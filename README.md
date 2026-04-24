# <img src="frontend/src/assets/logo/sadsa-full.png" alt="SADSA Logo" width="300"/>
# SADSA – Automated Agricultural Subsidy Request System

## Project Overview
  ![Project Type Selection](Screenshots/Phase%201/interface_selection_projet.png)
  ![General Info](Screenshots/Phase%201/interface_saisie_infos.png)
  ![Confirmation](Screenshots/Phase%201/interface_confirmation_creation.png)
- **Field Visit Geolocation:** Integrates GPS capture and photographic documentation for field inspections.
- **Real-Time Tracking:** Provides real-time status and history for all subsidy requests.
  ![GUC Dossiers](Screenshots/Phase%202/interface_guc_dossiers_soumis.png)
  ![Verification](Screenshots/Phase%202/interface_verification_dossier.png)
  ![Send to Commission](Screenshots/Phase%202/interface_envoi_aux_commission.png)

### Common Interfaces
  ![Commission Dossiers](Screenshots/Phase%203/liste_dossier_commission.png)
  ![Schedule Visit](Screenshots/Phase%203/planer_une_visite_commission.png)
  ![Planned Visits](Screenshots/Phase%203/visites_terrain_commission.png)
  ![Finalize Visit](Screenshots/Phase%203/finaliser_visite_commission.png)
### Phase 1: Antenna Agent
- **Project Type Selection:**
  ![Approve Dossier](Screenshots/Phase%204/approve_dossier_detail.png)
  ![Consult Dossier](Screenshots/Phase%204/consulter_dossier_guc_final.png)
  ![Approval Sheet](Screenshots/Phase%204/fiche_approbation.png)
  ![Confirmation](Screenshots/Phase 1/interface_confirmation_creation.png)
- **Receipt Generation:**
  ![Approved Dossiers](Screenshots/Phase%205/antenne_dossier_liste_approve.png)
  ![Start Realization](Screenshots/Phase%205/antenne_start_realisation.png)
- **Dossier Details:**
  ![Dossier Details](Screenshots/Phase 1/interface_details_dossier.png)
  ![Send to Service](Screenshots/Phase%206/guc_send_to_service.png)
  ![Service List](Screenshots/Phase%206/guc_liste_send_service.png)
  ![Send to GUC](Screenshots/Phase 1/interface_envoi_aux_guc.png)

  ![Service Dossiers](Screenshots/Phase%207/liste_dossiers_service.png)
  ![Schedule Technical Visit](Screenshots/Phase%207/planer_visite_service.png)
  ![Planned Implementation Visits](Screenshots/Phase%207/service_visites_planer.png)
  ![Service Dossier Details](Screenshots/Phase%207/service_dossier_detail.png)
  ![Finalize Visit](Screenshots/Phase%207/service_finaliser_visite.png)
  ![Send to Commission](Screenshots/Phase 2/interface_envoi_aux_commission.png)

  ![Manage Documents](Screenshots/Admin/admin_liste_documents_requis.png)
  ![Add Document](Screenshots/Admin/admin_add_document_requis.png)
  ![User Management](Screenshots/Admin/admin_liste_utilisateurs.png)
  ![Add User](Screenshots/Admin/admin_add_utilisateur.png)
  ![Edit User](Screenshots/Admin/admin_modifier_utilisateur.png)
  ![Planned Visits](Screenshots/Phase 3/visites_terrain_commission.png)
- **Finalize Visit:**
  ![Finalize Visit](Screenshots/Phase 3/finaliser_visite_commission.png)

### Phase 4: GUC Final Approval
  ![Project Type Selection](Screenshots/Phase%201/interface_selection_projet.png)
  ![General Info](Screenshots/Phase%201/interface_saisie_infos.png)
  ![Confirmation](Screenshots/Phase%201/interface_confirmation_creation.png)
  ![Receipt](Screenshots/Phase%201/interface_recepisse.png)
  ![Dossier List](Screenshots/Phase%201/interface_liste_dossiers.png)
  ![Dossier Details](Screenshots/Phase%201/interface_details_dossier.png)
  ![Document Scanning](Screenshots/Phase%201/interface_numerisation_formulaires.png)
  ![Send to GUC](Screenshots/Phase%201/interface_envoi_aux_guc.png)
  ![Approved Dossiers](Screenshots/Phase 5/antenne_dossier_liste_approve.png)
- **Start Realization:**
  ![GUC Dossiers](Screenshots/Phase%202/interface_guc_dossiers_soumis.png)
  ![Verification](Screenshots/Phase%202/interface_verification_dossier.png)
  ![Send to Commission](Screenshots/Phase%202/interface_envoi_aux_commission.png)
  ![Send to Service](Screenshots/Phase 6/guc_send_to_service.png)
- **Service List:**
  ![Commission Dossiers](Screenshots/Phase%203/liste_dossier_commission.png)
  ![Schedule Visit](Screenshots/Phase%203/planer_une_visite_commission.png)
  ![Planned Visits](Screenshots/Phase%203/visites_terrain_commission.png)
  ![Finalize Visit](Screenshots/Phase%203/finaliser_visite_commission.png)
- **Schedule Technical Visit:**
  ![Schedule Technical Visit](Screenshots/Phase 7/planer_visite_service.png)
  ![Approve Dossier](Screenshots/Phase%204/approve_dossier_detail.png)
  ![Consult Dossier](Screenshots/Phase%204/consulter_dossier_guc_final.png)
  ![Approval Sheet](Screenshots/Phase%204/fiche_approbation.png)
- **Finalize Visit:**
  ![Finalize Visit](Screenshots/Phase 7/service_finaliser_visite.png)
  ![Approved Dossiers](Screenshots/Phase%205/antenne_dossier_liste_approve.png)
  ![Start Realization](Screenshots/Phase%205/antenne_start_realisation.png)
  ![Manage Documents](Screenshots/Admin/admin_liste_documents_requis.png)
- **Add Required Document:**
  ![Send to Service](Screenshots/Phase%206/guc_send_to_service.png)
  ![Service List](Screenshots/Phase%206/guc_liste_send_service.png)
- **Add User:**
  ![Add User](Screenshots/Admin/admin_add_utilisateur.png)
  ![Service Dossiers](Screenshots/Phase%207/liste_dossiers_service.png)
  ![Schedule Technical Visit](Screenshots/Phase%207/planer_visite_service.png)
  ![Planned Implementation Visits](Screenshots/Phase%207/service_visites_planer.png)
  ![Service Dossier Details](Screenshots/Phase%207/service_dossier_detail.png)
  ![Finalize Visit](Screenshots/Phase%207/service_finaliser_visite.png)
- **Backend:** Java (Spring Boot, Spring Data JPA, Spring Security)
- **Frontend:** Vue.js (SPA) with PrimeVue UI components
- **Database:** MySQL (designed and managed with MySQL Workbench)
- **Authentication:** JWT (JSON Web Token) for stateless, secure API access
- **Development Tools:**
  - Visual Studio Code (IDE)
  - Astah UML (UML modeling)
  - Postman (API testing)
  - Inkscape (vector graphics)
  - Git & GitHub (version control)

### Main Entities
- **Dossier:** Central entity for subsidy requests, tracking status, applicant, and workflow history
- **Utilisateur:** System users with roles (Admin, Antenna Agent, GUC Agent, Commission, Technical Service)
- **Agriculteur:** Beneficiary farmers with personal and location data
- **VisiteTerrain / VisiteImplementation:** Field and implementation visits with geolocation and reports
- **PieceJointe:** Attached documents for each dossier

### Setup & Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/migi-gluttony/sadsa-ormvat.git
   ```
2. **Backend:**
   - Navigate to `backend/`
   - Configure `application.properties` for your MySQL instance
   - Build and run with Maven:
     ```bash
     ./mvnw spring-boot:run
     ```
3. **Frontend:**
   - Navigate to `frontend/`
   - Install dependencies:
     ```bash
     npm install
     ```
   - Start the development server:
     ```bash
     npm run dev
     ```

## License

This project is licensed under the MIT License.

---

*For more details, see the full project report and code documentation.*
