package com.jwtauth.example.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jwtauth.example.constants.Constants;
import com.jwtauth.example.dao.UserDao;
import com.jwtauth.example.model.User;
import com.jwtauth.example.util.CypherUtils;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Date;

@Path("/")
public class RESTService {

    private final Logger LOGGER = Logger.getLogger(RESTService.class);

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(User user) {
        ResponsePojo responsePojo = new ResponsePojo();
        // Check if user email and password are presented.
        Response res = validateUserProps(user);
        if (res != null) return res;

        // Save user to h2 database.
        UserDao userDao = new UserDao();
        boolean saveSuccess = userDao.save(user);

        // If save is not success then the email is already in use.
        if (!saveSuccess) {
            responsePojo.setError("Email is already in use!");
            return Response.status(422).entity(responsePojo).build();
        }

        responsePojo.setToken(generateToken(user));

        return Response.ok().entity(responsePojo).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(User user) {
        ResponsePojo responsePojo = new ResponsePojo();

        // Check if user email and password are presented.
        Response res = validateUserProps(user);
        if (res != null) return res;

        // Find user by email.
        UserDao userDao = new UserDao();
        User existingUser = userDao.findUserByEmail(user.getEmail());

        if (existingUser == null) {
            responsePojo.setError("User doesn't exists.");
            return Response.ok().entity(responsePojo).build();
        }

        if (existingUser.getPassword().equals(CypherUtils.getSHA512SecurePassword(user.getPassword()))) {
            responsePojo.setToken(generateToken(existingUser));
        } else {
            responsePojo.setError("Authentication failed.");
        }

        return Response.ok().entity(responsePojo).build();
    }

    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response protectedResource(@HeaderParam("authorization") String token) {
        User user = validateToken(token);
        if(user != null) {
            user.setPassword(null);
            return Response.ok().entity(user).build();
        } else {
            ResponsePojo responsePojo = new ResponsePojo();
            responsePojo.setError("Invalid token.");
            return Response.ok().entity(responsePojo).build();
        }
    }


    private String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(Constants.JWT_TOKEN_KEY);
            Date expirationDate = Date.from(ZonedDateTime.now().plusHours(24).toInstant());
            Date issuedAt = Date.from(ZonedDateTime.now().toInstant());
            return JWT.create()
                    .withIssuedAt(issuedAt) // Issue date.
                    .withExpiresAt(expirationDate) // Expiration date.
                    .withClaim("userId", user.getId()) // User id - here we can put anything we want, but for the example userId is appropriate.
                    .withIssuer("jwtauth") // Issuer of the token.
                    .sign(algorithm); // And the signing algorithm.
        } catch (UnsupportedEncodingException | JWTCreationException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    private User validateToken(String token) {
        try {
            if(token != null) {
                Algorithm algorithm = Algorithm.HMAC256(Constants.JWT_TOKEN_KEY);
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("jwtauth")
                        .build(); //Reusable verifier instance
                DecodedJWT jwt = verifier.verify(token);

                //Get the userId from token claim.
                Claim userId = jwt.getClaim("userId");

                // Find user by token subject(id).
                UserDao userDao = new UserDao();
                return userDao.findUserById(userId.asLong());
            }
        } catch (UnsupportedEncodingException | JWTVerificationException e){
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    private Response validateUserProps(User user) {
        ResponsePojo responsePojo = new ResponsePojo();
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            responsePojo.setError("Email is required!");
            return Response.status(422).entity(responsePojo).build();
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            responsePojo.setError("Password is required!");
            return Response.status(422).entity(responsePojo).build();
        }
        return null;
    }
}
