package com.jwtauth.example.services;


import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class ResponsePojo {

    @XmlElement
    private String token;

    @XmlElement
    private String error;

}
