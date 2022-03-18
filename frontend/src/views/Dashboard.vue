<template>
  <div class="content">
    <h1><b>Dashboard</b></h1>
    <div class="container">
      <div v-if="devices.length === 0">
        <h5><b>등록된 디바이스가 없습니다.</b></h5>
      </div>
      <div v-else>
        <ul>
          <li
              v-for="(device, i) in devices" :key="i"
              @click="toDetail(device.deviceId)"
          >
            {{ device.deviceName }}
          </li>
        </ul>
      </div>
      <b-button v-b-modal.deviceRegisterModal>등록하기</b-button>
      <b-modal
          id="deviceRegisterModal"
          ref="modal"
          title="등록하기"
          @show="resetModal"
          @hidden="resetModal"
          @ok="register"
      >
        <form
            ref="form"
            @submit.stop.prevent="handleSubmit"
        >
          <b-form-group
              label="Device Name"
              label-for="name-input"
              invalid-feedback="Required"
              :state="regNameState"
          >
            <b-form-input
                id="name-input"
                v-model="regName"
                :state="regNameState"
                required
            />
          </b-form-group>
        </form>
      </b-modal>
    </div>
  </div>
</template>

<script>
export default {
  name: "Dashboard",
  created() {
    this.$axios.get('/api/devices?direction=ASC&page=1&size=10')
        .then(res => {
          this.devices = res.data.content;
        })
        .catch(error => {
          this.devices = error;
        })
  },
  methods: {
    toDetail(deviceId) {
      window.location.href='/devices?deviceId='+deviceId;
    },
    register(bvModalEvt) {
      bvModalEvt.preventDefault();
      this.$axios({
        method: 'POST',
        url: '/api/devices',
        data: JSON.stringify(this.regName),
        headers: {'Content-Type': 'application/json; charset=utf-8'}
      });
      this.handleSubmit();
      window.location.reload();
    },
    handleSubmit() {
      if (!this.checkFormValidity()) {
        return
      }
      this.$nextTick(() => {
        this.$bvModal.hide('deviceRegisterModal')
      })
    },
    checkFormValidity() {
      const valid = this.$refs.form.checkValidity()
      this.regNameState = valid
      return valid
    },
    resetModal() {
      this.regName = ''
      this.regNameState = null
    }
  },

  data() {
    return {
      devices: [],
      regName: '',
      regNameState: null,
    }
  }
}
</script>

<style scoped>

</style>