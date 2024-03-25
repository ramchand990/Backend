package com.healspan.claimservice.mstupload.claim.exception;

public class InvalidStatusException extends RuntimeException{
    public InvalidStatusException(String message){
        super(message);
    }
}
