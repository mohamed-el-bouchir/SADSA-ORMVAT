package ormvat.sadsa.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import ormvat.sadsa.model.Utilisateur.UserRole;
import ormvat.sadsa.model.Utilisateur.EquipeCommission;

import java.time.LocalDateTime;
import java.util.List;

public class AdminUserManagementDTOs {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserListResponse {
        private List<UserSummaryDTO> users;
        private Long totalUsers;
        private Long activeUsers;
        private Long inactiveUsers;
        private UserStatsDTO stats;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserSummaryDTO {
        private Long id;
        private String nom;
        private String prenom;
        private String email;
        private String telephone;
        private UserRole role;
        private EquipeCommission equipeCommission;
        private String statut;
        private Boolean actif;
        private LocalDateTime dateCreation;
        private LocalDateTime derniereConnexion;
        private String antenneDesignation;
        private String antenneAbreviation;
        private Long antenneId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDetailResponse {
        private Long id;
        private String nom;
        private String prenom;
        private String email;
        private String telephone;
        private UserRole role;
        private EquipeCommission equipeCommission;
        private String statut;
        private Boolean actif;
        private LocalDateTime dateCreation;
        private LocalDateTime derniereConnexion;
        private AntenneDTO antenne;
        private List<UserActivityDTO> recentActivities;
        private UserStatsDTO individualStats;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AntenneDTO {
        private Long id;
        private String designation;
        private String abreviation;
        private String cdaDescription;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserActivityDTO {
        private String action;
        private String description;
        private LocalDateTime timestamp;
        private String entityType;
        private Long entityId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserStatsDTO {
        private Long totalByRole;
        private Long totalDossiers;
        private Long dossiersCreated;
        private Long dossiersProcessed;
        private Long notesCreated;
        private Long lastActivityDays;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateUserRequest {
        @NotBlank(message = "Le nom est requis")
        private String nom;
        
        @NotBlank(message = "Le prénom est requis")
        private String prenom;
        
        @NotBlank(message = "L'email est requis")
        @Email(message = "Format d'email invalide")
        private String email;
        
        private String telephone;
        
        @NotBlank(message = "Le mot de passe est requis")
        private String motDePasse;
        
        @NotNull(message = "Le rôle est requis")
        private UserRole role;
        
        private Long antenneId;
        private EquipeCommission equipeCommission;
        
        @Builder.Default
        private String statut = "actif";
        
        @Builder.Default
        private Boolean actif = true;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateUserRequest {
        @NotNull(message = "L'ID utilisateur est requis")
        private Long id;
        
        @NotBlank(message = "Le nom est requis")
        private String nom;
        
        @NotBlank(message = "Le prénom est requis")
        private String prenom;
        
        @NotBlank(message = "L'email est requis")
        @Email(message = "Format d'email invalide")
        private String email;
        
        private String telephone;
        
        @NotNull(message = "Le rôle est requis")
        private UserRole role;
        
        private Long antenneId;
        private EquipeCommission equipeCommission;
        private String statut;
        private Boolean actif;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangePasswordRequest {
        @NotNull(message = "L'ID utilisateur est requis")
        private Long userId;
        
        @NotBlank(message = "Le nouveau mot de passe est requis")
        private String newPassword;
        
        @Builder.Default
        private Boolean forceChange = false;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ToggleUserStatusRequest {
        @NotNull(message = "L'ID utilisateur est requis")
        private Long userId;
        
        @NotNull(message = "Le statut actif est requis")
        private Boolean actif;
        
        private String reason;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserActionResponse {
        private Boolean success;
        private String message;
        private LocalDateTime timestamp;
        private UserSummaryDTO updatedUser;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserFilterRequest {
        private String searchTerm;
        private UserRole role;
        private Boolean actif;
        private Long antenneId;
        private EquipeCommission equipeCommission;
        private Integer page;
        private Integer size;
        private String sortBy;
        private String sortDirection;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AvailableOptionsResponse {
        private List<RoleOptionDTO> roles;
        private List<AntenneOptionDTO> antennes;
        private List<EquipeOptionDTO> equipes;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleOptionDTO {
        private UserRole value;
        private String label;
        private String description;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AntenneOptionDTO {
        private Long value;
        private String label;
        private String abreviation;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EquipeOptionDTO {
        private EquipeCommission value;
        private String label;
        private String description;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BulkActionRequest {
        @NotNull(message = "Les IDs des utilisateurs sont requis")
        private List<Long> userIds;
        
        @NotBlank(message = "L'action est requise")
        private String action; // "activate", "deactivate", "delete"
        
        private String reason;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BulkActionResponse {
        private Boolean success;
        private String message;
        private Integer successCount;
        private Integer failureCount;
        private List<String> errors;
        private LocalDateTime timestamp;
    }
}