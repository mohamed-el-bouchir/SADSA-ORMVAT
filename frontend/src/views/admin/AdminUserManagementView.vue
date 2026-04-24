<template>
  <div class="user-management-admin compact-layout">
    <!-- Compact Header with Statistics -->
    <div class="component-card compact-header">
      <div class="header-content">
        <div class="header-info">
          <h2><i class="pi pi-users"></i> Gestion des Comptes Utilisateurs</h2>
          <p>Gérez tous les comptes utilisateurs du système SADSA</p>
        </div>
        
        <!-- Inline Statistics -->
        <div class="inline-stats" v-if="statistics">
          <div class="stat-item">
            <div class="stat-icon"><i class="pi pi-users"></i></div>
            <div class="stat-content">
              <span class="stat-number">{{ statistics.totalUsers }}</span>
              <span class="stat-label">Total</span>
            </div>
          </div>
          <div class="stat-item">
            <div class="stat-icon success"><i class="pi pi-check-circle"></i></div>
            <div class="stat-content">
              <span class="stat-number">{{ statistics.activeUsers }}</span>
              <span class="stat-label">Actifs</span>
            </div>
          </div>
          <div class="stat-item">
            <div class="stat-icon danger"><i class="pi pi-times-circle"></i></div>
            <div class="stat-content">
              <span class="stat-number">{{ statistics.inactiveUsers }}</span>
              <span class="stat-label">Inactifs</span>
            </div>
          </div>
        </div>
      </div>
    </div>    <!-- Filters and Actions -->
    <Card class="mb-4">
      <template #content>
        <div class="grid">
          <div class="col-12 md:col-3">
            <label class="block text-900 font-medium mb-2">Rechercher</label>
            <InputText 
              v-model="filters.searchTerm"
              placeholder="Nom, prénom, email..."
              @input="debounceSearch"
              class="w-full"
            />
          </div>
          
          <div class="col-12 md:col-2">
            <label class="block text-900 font-medium mb-2">Rôle</label>
            <Dropdown 
              v-model="filters.role"
              :options="roleOptions"
              optionLabel="label"
              optionValue="value"
              placeholder="Tous les rôles"
              @change="applyFilters"
              class="w-full"
              :clearable="true"
            />
          </div>
          
          <div class="col-12 md:col-2">
            <label class="block text-900 font-medium mb-2">Statut</label>
            <Dropdown 
              v-model="filters.actif"
              :options="statusOptions"
              optionLabel="label"
              optionValue="value"
              placeholder="Tous les statuts"
              @change="applyFilters"
              class="w-full"
              :clearable="true"
            />
          </div>
          
          <div class="col-12 md:col-2">
            <label class="block text-900 font-medium mb-2">Antenne</label>
            <Dropdown 
              v-model="filters.antenneId"
              :options="antenneOptions"
              optionLabel="label"
              optionValue="value"
              placeholder="Toutes les antennes"
              @change="applyFilters"
              class="w-full"
              :clearable="true"
            />
          </div>
          
          <div class="col-12 md:col-3 flex align-items-end">
            <div class="flex gap-2 w-full">
              <Button 
                label="Nouveau" 
                icon="pi pi-plus" 
                @click="openCreateDialog"
                class="p-button-primary flex-1"
              />
              <Button 
                label="Actualiser" 
                icon="pi pi-refresh" 
                @click="loadUsers"
                class="p-button-outlined"
                :loading="loading"
              />
            </div>
          </div>
        </div>
      </template>
    </Card>    <!-- Users Table -->
    <Card>
      <template #content>
        <DataTable 
          :value="users" 
          :loading="loading"
          :paginator="true"
          :rows="20"
          :totalRecords="totalRecords"
          :lazy="true"
          @page="onPageChange"
          @sort="onSort"
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
          :rowsPerPageOptions="[10, 20, 50]"
          currentPageReportTemplate="Affichage de {first} à {last} sur {totalRecords} utilisateurs"
          responsiveLayout="scroll"
          :rowClass="getRowClass"
          class="users-table"
        >
          <template #empty>
            <div class="text-center p-6">
              <i class="pi pi-users text-4xl text-600 mb-3"></i>
              <h3>Aucun utilisateur trouvé</h3>
              <p class="text-600">Aucun utilisateur ne correspond aux critères de recherche.</p>
            </div>
          </template>

          <template #loading>
            <div class="text-center p-6">
              <ProgressSpinner size="50px" />
              <div class="mt-3">Chargement des utilisateurs...</div>
            </div>
          </template>

        <Column field="nom" header="Nom" sortable>
          <template #body="{ data }">
            <div class="user-name">
              <strong>{{ data.nom }} {{ data.prenom }}</strong>
              <small>{{ data.email }}</small>
            </div>
          </template>
        </Column>

        <Column field="role" header="Rôle" sortable>
          <template #body="{ data }">
            <Tag 
              :value="getRoleLabel(data.role)"
              :severity="getRoleSeverity(data.role)"
              class="role-tag"
            />
          </template>
        </Column>

        <Column field="antenne" header="Antenne">
          <template #body="{ data }">
            <span v-if="data.antenneDesignation" class="antenne-info">
              <i class="pi pi-building"></i>
              {{ data.antenneAbreviation || data.antenneDesignation }}
            </span>
            <span v-else class="no-antenne">-</span>
          </template>
        </Column>

        <Column field="equipeCommission" header="Équipe">
          <template #body="{ data }">
            <Tag 
              v-if="data.equipeCommission"
              :value="getEquipeLabel(data.equipeCommission)"
              severity="info"
              class="equipe-tag"
            />
            <span v-else>-</span>
          </template>
        </Column>

        <Column field="actif" header="Statut" sortable>
          <template #body="{ data }">
            <Tag 
              :value="data.actif ? 'Actif' : 'Inactif'"
              :severity="data.actif ? 'success' : 'danger'"
              :icon="data.actif ? 'pi pi-check' : 'pi pi-times'"
              class="status-tag"
            />
          </template>
        </Column>

        <Column field="dateCreation" header="Créé le" sortable>
          <template #body="{ data }">
            <span class="date-display">
              {{ formatDate(data.dateCreation) }}
            </span>
          </template>
        </Column>

        <Column field="derniereConnexion" header="Dernière connexion">
          <template #body="{ data }">
            <span v-if="data.derniereConnexion" class="date-display">
              {{ formatDate(data.derniereConnexion) }}
            </span>
            <span v-else class="no-connection">Jamais connecté</span>
          </template>
        </Column>

        <Column header="Actions" :exportable="false">
          <template #body="{ data }">
            <div class="action-buttons-row">
              <Button 
                icon="pi pi-eye" 
                @click="viewUser(data)"
                class="p-button-rounded p-button-text p-button-info"
                v-tooltip.top="'Voir détails'"
              />
              <Button 
                icon="pi pi-pencil" 
                @click="editUser(data)"
                class="p-button-rounded p-button-text p-button-warning"
                v-tooltip.top="'Modifier'"
              />
              <Button 
                :icon="data.actif ? 'pi pi-ban' : 'pi pi-check'" 
                @click="toggleUserStatus(data)"
                :class="data.actif ? 'p-button-rounded p-button-text p-button-danger' : 'p-button-rounded p-button-text p-button-success'"
                :v-tooltip.top="data.actif ? 'Désactiver' : 'Activer'"
              />
              <Button 
                icon="pi pi-key" 
                @click="resetPassword(data)"
                class="p-button-rounded p-button-text p-button-secondary"
                v-tooltip.top="'Réinitialiser mot de passe'"
              />
              <Button 
                icon="pi pi-trash" 
                @click="confirmDeleteUser(data)"
                class="p-button-rounded p-button-text p-button-danger"
                v-tooltip.top="'Supprimer'"
              />
            </div>
          </template>        </Column>
      </DataTable>
      </template>
    </Card>    <!-- Create/Edit User Dialog -->
    <Dialog 
      v-model:visible="showUserDialog" 
      :header="dialogMode === 'create' ? 'Créer un Utilisateur' : 'Modifier l\'Utilisateur'"
      modal 
      :style="{ width: '650px' }"
      :closable="true"
      class="compact-dialog"
    >      <form @submit.prevent="submitUserForm" class="document-form compact">
        <div class="compact-form-layout">
          <!-- Row 1: Nom et Prénom -->
          <div class="form-row">
            <div class="form-group">
              <label for="nom" class="required">Nom</label>
              <InputText 
                id="nom"
                v-model="userForm.nom"
                placeholder="Nom de famille"
                :class="{ 'p-invalid': formErrors.nom }"
                required
              />
              <small class="p-error">{{ formErrors.nom }}</small>
            </div>

            <div class="form-group">
              <label for="prenom" class="required">Prénom</label>
              <InputText 
                id="prenom"
                v-model="userForm.prenom"
                placeholder="Prénom"
                :class="{ 'p-invalid': formErrors.prenom }"
                required
              />
              <small class="p-error">{{ formErrors.prenom }}</small>
            </div>
          </div>

          <!-- Row 2: Email et Téléphone -->
          <div class="form-row">
            <div class="form-group">
              <label for="email" class="required">Email</label>
              <InputText 
                id="email"
                v-model="userForm.email"
                type="email"
                placeholder="adresse@ormvat.ma"
                :class="{ 'p-invalid': formErrors.email }"
                required
              />
              <small class="p-error">{{ formErrors.email }}</small>
            </div>

            <div class="form-group">
              <label for="telephone">Téléphone</label>
              <InputText 
                id="telephone"
                v-model="userForm.telephone"
                type="tel"
                placeholder="+212 6XX XXX XXX"
                :class="{ 'p-invalid': formErrors.telephone }"
              />
              <small class="p-error">{{ formErrors.telephone }}</small>
            </div>
          </div>

          <!-- Row 3: Rôle et Statut -->
          <div class="form-row">
            <div class="form-group">
              <label for="role" class="required">Rôle</label>
              <Dropdown 
                id="role"
                v-model="userForm.role"
                :options="roleOptions"
                optionLabel="label"
                optionValue="value"
                placeholder="Sélectionnez un rôle"
                :class="{ 'p-invalid': formErrors.role }"
                @change="onRoleChange"
                required
              />
              <small class="p-error">{{ formErrors.role }}</small>
            </div>

            <div class="form-group">
              <label for="statut">Statut</label>
              <div class="status-toggle">
                <div class="toggle-option">
                  <RadioButton 
                    id="actif-oui"
                    v-model="userForm.actif"
                    :value="true"
                  />
                  <label for="actif-oui">Actif</label>
                </div>
                <div class="toggle-option">
                  <RadioButton 
                    id="actif-non"
                    v-model="userForm.actif"
                    :value="false"
                  />
                  <label for="actif-non">Inactif</label>
                </div>
              </div>
            </div>
          </div>

          <!-- Row 4: Antenne (conditionnelle) -->
          <div v-if="userForm.role === 'AGENT_ANTENNE'" class="form-row">
            <div class="form-group full-width">
              <label for="antenne" class="required">Antenne</label>
              <Dropdown 
                id="antenne"
                v-model="userForm.antenneId"
                :options="antenneOptions"
                optionLabel="label"
                optionValue="value"
                placeholder="Sélectionnez une antenne"
                :class="{ 'p-invalid': formErrors.antenneId }"
                required
              />
              <small class="p-error">{{ formErrors.antenneId }}</small>
            </div>
          </div>

          <!-- Row 5: Équipe Commission (conditionnelle) -->
          <div v-if="userForm.role === 'AGENT_COMMISSION_TERRAIN'" class="form-row">
            <div class="form-group full-width">
              <label for="equipe" class="required">Équipe de Commission</label>
              <Dropdown 
                id="equipe"
                v-model="userForm.equipeCommission"
                :options="equipeOptions"
                optionLabel="label"
                optionValue="value"
                placeholder="Sélectionnez une équipe"
                :class="{ 'p-invalid': formErrors.equipeCommission }"
                required
              />
              <small class="p-error">{{ formErrors.equipeCommission }}</small>
            </div>
          </div>

          <!-- Row 6: Mot de passe (seulement pour création) -->
          <div v-if="dialogMode === 'create'" class="form-row">
            <div class="form-group full-width">
              <label for="password" class="required">Mot de passe</label>
              <Password 
                id="password"
                v-model="userForm.motDePasse"
                :feedback="true"
                toggleMask
                placeholder="Mot de passe sécurisé"
                :class="{ 'p-invalid': formErrors.motDePasse }"
                required
              />
              <small class="p-error">{{ formErrors.motDePasse }}</small>
            </div>
          </div>
        </div>
      </form>

      <template #footer>
        <Button 
          label="Annuler" 
          icon="pi pi-times" 
          @click="closeUserDialog"
          class="p-button-outlined" 
        />
        <Button 
          :label="dialogMode === 'create' ? 'Créer' : 'Mettre à jour'"
          :icon="dialogMode === 'create' ? 'pi pi-plus' : 'pi pi-check'"
          @click="submitUserForm"
          :loading="submitting"
          :disabled="!isFormValid"
          class="btn-primary"
        />
      </template>
    </Dialog>

    <!-- User Details Dialog -->
    <Dialog 
      v-model:visible="showDetailsDialog" 
      header="Détails de l'Utilisateur"
      modal 
      :style="{ width: '800px' }"
      class="details-dialog"
    >
      <div v-if="selectedUser" class="user-details">
        <div class="details-header">
          <div class="user-avatar">
            <i class="pi pi-user"></i>
          </div>
          <div class="user-info">
            <h3>{{ selectedUser.nom }} {{ selectedUser.prenom }}</h3>
            <p>{{ selectedUser.email }}</p>
            <Tag 
              :value="getRoleLabel(selectedUser.role)"
              :severity="getRoleSeverity(selectedUser.role)"
            />
          </div>
        </div>

        <div class="details-content">
          <div class="detail-section">
            <h4>Informations Générales</h4>
            <div class="detail-grid">
              <div class="detail-item">
                <label>Téléphone:</label>
                <span>{{ selectedUser.telephone || 'Non renseigné' }}</span>
              </div>
              <div class="detail-item">
                <label>Statut:</label>
                <Tag 
                  :value="selectedUser.actif ? 'Actif' : 'Inactif'"
                  :severity="selectedUser.actif ? 'success' : 'danger'"
                />
              </div>
              <div class="detail-item">
                <label>Date de création:</label>
                <span>{{ formatDate(selectedUser.dateCreation) }}</span>
              </div>
              <div class="detail-item">
                <label>Dernière connexion:</label>
                <span>{{ selectedUser.derniereConnexion ? formatDate(selectedUser.derniereConnexion) : 'Jamais connecté' }}</span>
              </div>
            </div>
          </div>

          <div v-if="selectedUser.antenneDesignation" class="detail-section">
            <h4>Antenne</h4>
            <div class="antenne-info">
              <i class="pi pi-building"></i>
              <span>{{ selectedUser.antenneDesignation }}</span>
            </div>
          </div>

          <div v-if="selectedUser.equipeCommission" class="detail-section">
            <h4>Équipe de Commission</h4>
            <Tag 
              :value="getEquipeLabel(selectedUser.equipeCommission)"
              severity="info"
            />
          </div>
        </div>
      </div>
    </Dialog>

    <!-- Password Reset Dialog -->
    <Dialog 
      v-model:visible="showPasswordDialog" 
      header="Réinitialiser le Mot de Passe"
      modal 
      :style="{ width: '500px' }"
      class="password-dialog"
    >
      <div class="password-reset-form">
        <div class="warning-message">
          <i class="pi pi-exclamation-triangle"></i>
          <p>Vous allez réinitialiser le mot de passe de <strong>{{ selectedUser?.nom }} {{ selectedUser?.prenom }}</strong></p>
        </div>

        <div class="form-group">
          <label for="newPassword" class="required">Nouveau mot de passe</label>
          <Password 
            id="newPassword"
            v-model="newPassword"
            :feedback="true"
            toggleMask
            placeholder="Nouveau mot de passe sécurisé"
            :class="{ 'p-invalid': passwordError }"
            required
          />
          <small class="p-error">{{ passwordError }}</small>
        </div>
      </div>

      <template #footer>
        <Button 
          label="Annuler" 
          icon="pi pi-times" 
          @click="closePasswordDialog"
          class="p-button-outlined" 
        />
        <Button 
          label="Réinitialiser" 
          icon="pi pi-key" 
          @click="confirmPasswordReset"
          :loading="submitting"
          :disabled="!newPassword || newPassword.length < 6"
          class="p-button-danger"
        />
      </template>
    </Dialog>

    <!-- Confirmation Dialogs -->
    <ConfirmDialog />

    <!-- Toast Messages -->
    <Toast />
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import { useToast } from 'primevue/usetoast';
import { useConfirm } from 'primevue/useconfirm';
import axios from 'axios';

// PrimeVue Components
import Button from 'primevue/button';
import Card from 'primevue/card';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Dropdown from 'primevue/dropdown';
import RadioButton from 'primevue/radiobutton';
import Password from 'primevue/password';
import Tag from 'primevue/tag';
import ProgressSpinner from 'primevue/progressspinner';
import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';

const toast = useToast();
const confirm = useConfirm();

// Reactive Data
const loading = ref(true);
const submitting = ref(false);
const users = ref([]);
const totalRecords = ref(0);
const statistics = ref(null);

// Dialog Management
const showUserDialog = ref(false);
const showDetailsDialog = ref(false);
const showPasswordDialog = ref(false);
const dialogMode = ref('create'); // 'create' or 'edit'
const selectedUser = ref(null);

// Form Data
const userForm = ref({
  id: null,
  nom: '',
  prenom: '',
  email: '',
  telephone: '',
  motDePasse: '',
  role: null,
  antenneId: null,
  equipeCommission: null,
  actif: true
});

const newPassword = ref('');
const passwordError = ref('');
const formErrors = ref({});

// Filters
const filters = ref({
  searchTerm: '',
  role: null,
  actif: null,
  antenneId: null,
  page: 0,
  size: 20,
  sortBy: 'dateCreation',
  sortDirection: 'DESC'
});

// Options
const roleOptions = ref([]);
const antenneOptions = ref([]);
const equipeOptions = ref([]);

const statusOptions = ref([
  { label: 'Actif', value: true },
  { label: 'Inactif', value: false }
]);

// API Base URL
const API_BASE = '/admin/users';

// Get auth headers
const getAuthHeaders = () => {
  const token = localStorage.getItem('token') || sessionStorage.getItem('token');
  return token ? { Authorization: `Bearer ${token}` } : {};
};

// Computed Properties
const isFormValid = computed(() => {
  return userForm.value.nom.trim() !== '' && 
         userForm.value.prenom.trim() !== '' && 
         userForm.value.email.trim() !== '' && 
         userForm.value.role !== null &&
         (dialogMode.value === 'edit' || userForm.value.motDePasse.trim() !== '') &&
         Object.keys(formErrors.value).length === 0;
});

// Methods
async function loadUsers() {
  try {
    loading.value = true;
    const params = new URLSearchParams();
    
    Object.keys(filters.value).forEach(key => {
      if (filters.value[key] !== null && filters.value[key] !== '') {
        params.append(key, filters.value[key]);
      }
    });

    const response = await axios.get(`${API_BASE}?${params}`, {
      headers: getAuthHeaders()
    });
    
    users.value = response.data.users || [];
    totalRecords.value = response.data.totalUsers || 0;
    statistics.value = {
      totalUsers: response.data.totalUsers || 0,
      activeUsers: response.data.activeUsers || 0,
      inactiveUsers: response.data.inactiveUsers || 0
    };
    
  } catch (error) {
    console.error('Erreur lors du chargement des utilisateurs:', error);
    toast.add({
      severity: 'error',
      summary: 'Erreur',
      detail: 'Impossible de charger les utilisateurs',
      life: 5000
    });
  } finally {
    loading.value = false;
  }
}

async function loadOptions() {
  try {
    const response = await axios.get(`${API_BASE}/options`, {
      headers: getAuthHeaders()
    });
    
    roleOptions.value = response.data.roles || [];
    antenneOptions.value = response.data.antennes || [];
    equipeOptions.value = response.data.equipes || [];
    
  } catch (error) {
    console.error('Erreur lors du chargement des options:', error);
  }
}

async function createUser() {
  try {
    submitting.value = true;
    
    const userData = {
      nom: userForm.value.nom.trim(),
      prenom: userForm.value.prenom.trim(),
      email: userForm.value.email.trim(),
      telephone: userForm.value.telephone?.trim() || null,
      motDePasse: userForm.value.motDePasse,
      role: userForm.value.role,
      antenneId: userForm.value.antenneId,
      equipeCommission: userForm.value.equipeCommission,
      actif: userForm.value.actif
    };

    await axios.post(API_BASE, userData, {
      headers: getAuthHeaders()
    });
    
    toast.add({
      severity: 'success',
      summary: 'Succès',
      detail: 'Utilisateur créé avec succès',
      life: 3000
    });
    
    closeUserDialog();
    await loadUsers();
    
  } catch (error) {
    console.error('Erreur lors de la création:', error);
    toast.add({
      severity: 'error',
      summary: 'Erreur',
      detail: error.response?.data?.message || 'Erreur lors de la création',
      life: 5000
    });
  } finally {
    submitting.value = false;
  }
}

async function updateUser() {
  try {
    submitting.value = true;
    
    const userData = {
      id: userForm.value.id,
      nom: userForm.value.nom.trim(),
      prenom: userForm.value.prenom.trim(),
      email: userForm.value.email.trim(),
      telephone: userForm.value.telephone?.trim() || null,
      role: userForm.value.role,
      antenneId: userForm.value.antenneId,
      equipeCommission: userForm.value.equipeCommission,
      actif: userForm.value.actif
    };

    await axios.put(`${API_BASE}/${userData.id}`, userData, {
      headers: getAuthHeaders()
    });
    
    toast.add({
      severity: 'success',
      summary: 'Succès',
      detail: 'Utilisateur mis à jour avec succès',
      life: 3000
    });
    
    closeUserDialog();
    await loadUsers();
    
  } catch (error) {
    console.error('Erreur lors de la mise à jour:', error);
    toast.add({
      severity: 'error',
      summary: 'Erreur',
      detail: error.response?.data?.message || 'Erreur lors de la mise à jour',
      life: 5000
    });
  } finally {
    submitting.value = false;
  }
}

async function toggleUserStatus(user) {
  const action = user.actif ? 'désactiver' : 'activer';
  const message = user.actif ? 
    `Êtes-vous sûr de vouloir désactiver ${user.nom} ${user.prenom} ?` :
    `Êtes-vous sûr de vouloir activer ${user.nom} ${user.prenom} ?`;

  confirm.require({
    message,
    header: `Confirmer ${action}`,
    icon: 'pi pi-exclamation-triangle',
    acceptClass: user.actif ? 'p-button-danger' : 'p-button-success',
    acceptLabel: action.charAt(0).toUpperCase() + action.slice(1),
    rejectLabel: 'Annuler',
    accept: async () => {
      try {
        await axios.put(`${API_BASE}/${user.id}/status`, {
          userId: user.id,
          actif: !user.actif
        }, {
          headers: getAuthHeaders()
        });
        
        toast.add({
          severity: 'success',
          summary: 'Succès',
          detail: `Utilisateur ${action} avec succès`,
          life: 3000
        });
        
        await loadUsers();
        
      } catch (error) {
        console.error(`Erreur lors du ${action}:`, error);
        toast.add({
          severity: 'error',
          summary: 'Erreur',
          detail: error.response?.data?.message || `Erreur lors du ${action}`,
          life: 5000
        });
      }
    }
  });
}

async function confirmPasswordReset() {
  if (!newPassword.value || newPassword.value.length < 6) {
    passwordError.value = 'Le mot de passe doit contenir au moins 6 caractères';
    return;
  }

  try {
    submitting.value = true;
    
    await axios.put(`${API_BASE}/${selectedUser.value.id}/password`, {
      userId: selectedUser.value.id,
      newPassword: newPassword.value
    }, {
      headers: getAuthHeaders()
    });
    
    toast.add({
      severity: 'success',
      summary: 'Succès',
      detail: 'Mot de passe réinitialisé avec succès',
      life: 3000
    });
    
    closePasswordDialog();
    
  } catch (error) {
    console.error('Erreur lors de la réinitialisation:', error);
    toast.add({
      severity: 'error',
      summary: 'Erreur',
      detail: error.response?.data?.message || 'Erreur lors de la réinitialisation',
      life: 5000
    });
  } finally {
    submitting.value = false;
  }
}

function confirmDeleteUser(user) {
  confirm.require({
    message: `Êtes-vous sûr de vouloir supprimer ${user.nom} ${user.prenom} ?`,
    header: 'Confirmer la suppression',
    icon: 'pi pi-exclamation-triangle',
    acceptClass: 'p-button-danger',
    acceptLabel: 'Supprimer',
    rejectLabel: 'Annuler',
    accept: async () => {
      try {
        await axios.delete(`${API_BASE}/${user.id}`, {
          headers: getAuthHeaders()
        });
        
        toast.add({
          severity: 'success',
          summary: 'Succès',
          detail: 'Utilisateur supprimé avec succès',
          life: 3000
        });
        
        await loadUsers();
        
      } catch (error) {
        console.error('Erreur lors de la suppression:', error);
        toast.add({
          severity: 'error',
          summary: 'Erreur',
          detail: error.response?.data?.message || 'Erreur lors de la suppression',
          life: 5000
        });
      }
    }
  });
}

// Dialog Management
function openCreateDialog() {
  dialogMode.value = 'create';
  resetForm();
  showUserDialog.value = true;
}

function editUser(user) {
  dialogMode.value = 'edit';
  userForm.value = {
    id: user.id,
    nom: user.nom,
    prenom: user.prenom,
    email: user.email,
    telephone: user.telephone || '',
    motDePasse: '',
    role: user.role,
    antenneId: user.antenneId,
    equipeCommission: user.equipeCommission,
    actif: user.actif
  };
  showUserDialog.value = true;
}

function viewUser(user) {
  selectedUser.value = user;
  showDetailsDialog.value = true;
}

function resetPassword(user) {
  selectedUser.value = user;
  newPassword.value = '';
  passwordError.value = '';
  showPasswordDialog.value = true;
}

function closeUserDialog() {
  showUserDialog.value = false;
  resetForm();
  formErrors.value = {};
}

function closePasswordDialog() {
  showPasswordDialog.value = false;
  selectedUser.value = null;
  newPassword.value = '';
  passwordError.value = '';
}

function resetForm() {
  userForm.value = {
    id: null,
    nom: '',
    prenom: '',
    email: '',
    telephone: '',
    motDePasse: '',
    role: null,
    antenneId: null,
    equipeCommission: null,
    actif: true
  };
}

function submitUserForm() {
  if (!validateForm()) {
    return;
  }
  
  if (dialogMode.value === 'create') {
    createUser();
  } else {
    updateUser();
  }
}

function validateForm() {
  formErrors.value = {};
  
  if (!userForm.value.nom.trim()) {
    formErrors.value.nom = 'Le nom est requis';
  }
  
  if (!userForm.value.prenom.trim()) {
    formErrors.value.prenom = 'Le prénom est requis';
  }
  
  if (!userForm.value.email.trim()) {
    formErrors.value.email = 'L\'email est requis';
  } else if (!/^\S+@\S+\.\S+$/.test(userForm.value.email)) {
    formErrors.value.email = 'Format d\'email invalide';
  }
  
  if (userForm.value.telephone && !/^[0-9+\s()-]{8,15}$/.test(userForm.value.telephone)) {
    formErrors.value.telephone = 'Format de téléphone invalide';
  }
  
  if (!userForm.value.role) {
    formErrors.value.role = 'Le rôle est requis';
  }
  
  if (dialogMode.value === 'create' && !userForm.value.motDePasse.trim()) {
    formErrors.value.motDePasse = 'Le mot de passe est requis';
  } else if (dialogMode.value === 'create' && userForm.value.motDePasse.length < 6) {
    formErrors.value.motDePasse = 'Le mot de passe doit contenir au moins 6 caractères';
  }
  
  // Role-specific validations
  if (userForm.value.role === 'AGENT_ANTENNE' && !userForm.value.antenneId) {
    formErrors.value.antenneId = 'L\'antenne est requise pour les agents d\'antenne';
  }
  
  if (userForm.value.role === 'AGENT_COMMISSION_TERRAIN' && !userForm.value.equipeCommission) {
    formErrors.value.equipeCommission = 'L\'équipe est requise pour les agents de commission';
  }
  
  return Object.keys(formErrors.value).length === 0;
}

function onRoleChange() {
  // Reset conditional fields when role changes
  if (userForm.value.role !== 'AGENT_ANTENNE') {
    userForm.value.antenneId = null;
  }
  if (userForm.value.role !== 'AGENT_COMMISSION_TERRAIN') {
    userForm.value.equipeCommission = null;
  }
  
  // Clear related errors
  delete formErrors.value.antenneId;
  delete formErrors.value.equipeCommission;
}

// Table event handlers
function onPageChange(event) {
  filters.value.page = event.page;
  filters.value.size = event.rows;
  loadUsers();
}

function onSort(event) {
  filters.value.sortBy = event.sortField;
  filters.value.sortDirection = event.sortOrder === 1 ? 'ASC' : 'DESC';
  loadUsers();
}

// Search and filters
let searchTimeout;
function debounceSearch() {
  clearTimeout(searchTimeout);
  searchTimeout = setTimeout(() => {
    applyFilters();
  }, 500);
}

function applyFilters() {
  filters.value.page = 0; // Reset to first page
  loadUsers();
}

// Utility functions
function formatDate(dateString) {
  if (!dateString) return '';
  return new Date(dateString).toLocaleDateString('fr-FR', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
}

function getRoleLabel(role) {
  const roleMap = {
    'ADMIN': 'Administrateur',
    'AGENT_ANTENNE': 'Agent d\'Antenne',
    'AGENT_GUC': 'Agent GUC',
    'AGENT_COMMISSION_TERRAIN': 'Agent Commission',
    'SERVICE_TECHNIQUE': 'Service Technique'
  };
  return roleMap[role] || role;
}

function getRoleSeverity(role) {
  const severityMap = {
    'ADMIN': 'danger',
    'AGENT_ANTENNE': 'success',
    'AGENT_GUC': 'info',
    'AGENT_COMMISSION_TERRAIN': 'warning',
    'SERVICE_TECHNIQUE': 'secondary'
  };
  return severityMap[role] || 'info';
}

function getEquipeLabel(equipe) {
  const equipeMap = {
    'FILIERES_VEGETALES': 'Filières Végétales',
    'FILIERES_ANIMALES': 'Filières Animales',
    'AMENAGEMENT_HYDRO_AGRICOLE': 'Aménagement Hydro-Agricole'
  };
  return equipeMap[equipe] || equipe;
}

function getRowClass(data) {
  return data.actif ? '' : 'inactive-user';
}

// Lifecycle
onMounted(async () => {
  await Promise.all([loadOptions(), loadUsers()]);
});
</script>

<style scoped>
/* ===== COMPACT LAYOUT STYLES (matching AdminDocumentRequisView.vue) ===== */

.user-management-admin.compact-layout {
  padding: 0;
}

/* Compact Header */
.compact-header {
  margin-bottom: 1.5rem;
}

.compact-header .header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 2rem;
}

.compact-header .header-info h2 {
  color: var(--primary-color);
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.compact-header .header-info p {
  color: var(--text-secondary);
  margin: 0;
  font-size: 0.95rem;
}

/* Inline Statistics */
.inline-stats {
  display: flex;
  gap: 1.5rem;
  align-items: center;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem 1rem;
  background: var(--surface-background);
  border-radius: var(--border-radius-md);
  border: 1px solid var(--border-color);
}

.stat-icon {
  width: 2.5rem;
  height: 2.5rem;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--primary-color);
  color: white;
  font-size: 1rem;
}

.stat-icon.success {
  background: var(--success-color);
}

.stat-icon.danger {
  background: var(--danger-color);
}

.stat-icon.info {
  background: var(--info-color);
}

.stat-content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.stat-number {
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--text-color);
  line-height: 1;
}

.stat-label {
  font-size: 0.8rem;
  color: var(--text-secondary);
  font-weight: 500;
}

/* Compact Dialog */
.compact-dialog :deep(.p-dialog-content) {
  padding: 1.5rem;
}

.compact-form-layout {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  font-weight: 600;
  color: var(--text-color);
  font-size: 0.9rem;
}

.form-group label.required::after {
  content: ' *';
  color: var(--danger-color);
}

/* Table Styling */
.users-table :deep(.p-datatable-header) {
  border: none;
  background: transparent;
  padding: 0 0 1rem 0;
}

.users-table :deep(.p-paginator) {
  border: none;
  background: transparent;
  padding: 1rem 0 0 0;
}

/* User Name Styling */
.user-name {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.user-name strong {
  color: var(--text-color);
  font-weight: 600;
}

.user-name small {
  color: var(--text-secondary);
  font-size: 0.8rem;
}

/* Antenne Info */
.antenne-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--text-color);
}

.antenne-info i {
  color: var(--primary-color);
}

.no-antenne, .no-connection {
  color: var(--text-muted);
  font-style: italic;
}

/* Date Display */
.date-display {
  color: var(--text-color);
  font-size: 0.9rem;
}

/* Action Buttons Row */
.action-buttons-row {
  display: flex;
  gap: 0.25rem;
  justify-content: center;
}

/* Detail View */
.details-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid var(--border-color);
}

.user-avatar {
  width: 4rem;
  height: 4rem;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: 700;
  color: white;
  background: var(--primary-color);
}

.user-basic-info h3 {
  margin: 0 0 0.5rem 0;
  color: var(--text-color);
  font-size: 1.25rem;
}

.user-basic-info p {
  margin: 0;
  color: var(--text-secondary);
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.detail-section h4 {
  color: var(--primary-color);
  margin: 0 0 1rem 0;
  font-size: 1.1rem;
  font-weight: 600;
  border-bottom: 2px solid var(--primary-color);
  padding-bottom: 0.5rem;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 0;
  border-bottom: 1px solid var(--border-color);
}

.detail-item:last-child {
  border-bottom: none;
}

.detail-item label {
  font-weight: 500;
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.detail-item span {
  color: var(--text-color);
}

/* Password Reset Form */
.warning-message {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem;
  background: #fef3cd;
  border: 1px solid #fbbf24;
  border-radius: var(--border-radius-md);
  margin-bottom: 1.5rem;
}

.warning-message i {
  color: #d97706;
  font-size: 1.25rem;
}

.warning-message p {
  margin: 0;
  color: #92400e;
}

/* Responsive Design */
@media (max-width: 1200px) {
  .compact-header .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 1.5rem;
  }
  
  .inline-stats {
    width: 100%;
    justify-content: space-between;
  }
}

@media (max-width: 992px) {
  .inline-stats {
    flex-direction: column;
    align-items: stretch;
    gap: 1rem;
  }
  
  .stat-item {
    justify-content: center;
  }
}

@media (max-width: 768px) {
  .user-management-admin.compact-layout {
    padding: 0;
  }
  
  .compact-header {
    margin-bottom: 1rem;
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .details-header {
    flex-direction: column;
    text-align: center;
  }
  
  .detail-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .action-buttons-row {
    flex-wrap: wrap;
    gap: 0.25rem;
  }
  
  .inline-stats {
    gap: 0.75rem;
  }
  
  .stat-item {
    padding: 0.75rem;
    min-width: auto;
    flex: 1;
  }
}
</style>