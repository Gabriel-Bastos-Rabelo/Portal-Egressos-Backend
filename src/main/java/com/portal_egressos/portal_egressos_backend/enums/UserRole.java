package com.portal_egressos.portal_egressos_backend.enums;

public enum UserRole {
    COORDENADOR("coordenador"),
    EGRESSO("egresso");
  
    private String role;
  
    UserRole(String role) {
      this.role = role;
    }
  
    public String getValue() {
      return role;
    }
  }