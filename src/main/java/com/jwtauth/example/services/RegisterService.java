package com.jwtauth.example.services;

import com.jwtauth.example.dao.UserDao;
import com.jwtauth.example.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class RegisterService {

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(User user) {
        Response res = validateUserProps(user);
        if (res != null) return res;

        UserDao userDao = new UserDao();
        boolean saveSuccess = userDao.save(user);

        if(!saveSuccess){
            ErrorPojo error = new ErrorPojo();
            error.setError("Email is already in use!");
            return Response.status(422).entity(error).build();
        }

        return Response.ok().entity(user).build();
    }

    private Response validateUserProps(User user) {
        ErrorPojo error = new ErrorPojo();
        if(user.getEmail() == null || user.getEmail().isEmpty()) {
            error.setError("Email is required!");
            return Response.status(422).entity(error).build();
        }
        if(user.getPassword() == null || user.getPassword().isEmpty()) {
            error.setError("Password is required!");
            return Response.status(422).entity(error).build();
        }
        return null;
    }
}
