package com.mindhub.homebanking.DTO;

public record NewClientApplicationDTO(String firstName, String lastName, String email,
                                      String password, Boolean createAdmin) {
}