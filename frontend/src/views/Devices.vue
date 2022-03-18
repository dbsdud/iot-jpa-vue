<template>
  <div class="content">
    <h1><b>{{ deviceInfo.deviceName }}</b></h1>
    <ul>
      <li>
        최초 등록일: {{ deviceInfo.createdAt }}
      </li>
      <li>
        최근 수정일: {{ deviceInfo.updatedAt }}
      </li>
    </ul>
    <div class="container">
      <line-chart class="small"
                  :chart-data="dataCollection"
      />
      {{ iotdata }}
    </div>
  </div>
</template>

<script>
import LineChart from "@/components/chart/LineChart";

export default {
  name: "Devices",
  components: {LineChart},
  data() {
    return {
      deviceId: this.$route.query.deviceId,
      deviceInfo: '',
      iotdata: null,
      dataCollection: null,
      chartOptions: null,
      createdAt: [],
      temperature: [],
      humidity: [],
      soilTemperature: [],
      soilHumidity: [],
      lux: [],
    }
  },
  created() {
    this.$axios.get('/api/devices/'+this.deviceId)
        .then(res => {
          this.deviceInfo = res.data;
        });
    this.getInitIotdata();
  },
  mounted() {
    setInterval(this.getRealtimeIotdata, 60000);
  },

  methods: {
    getInitIotdata() {
      this.$axios.get('/api/data?device=' + this.deviceId)
          .then(res => {
            this.iotdata = res.data;
            for (const i in this.iotdata) {
              this.createdAt.push(this.timeFormatter(this.iotdata[i].createdAt).time);
              this.temperature.push(this.iotdata[i].temperature);
              this.humidity.push(this.iotdata[i].humidity);
              this.soilTemperature.push(this.iotdata[i].soilTemperature);
              this.soilHumidity.push(this.iotdata[i].soilHumidity);
              this.lux.push(this.iotdata[i].lux);
            }
            this.dataCollection = {
              labels: this.createdAt.reverse(),
              datasets: [
                {
                  label: '온도',
                  backgroundColor: 'rgba(248,121,121,0.5)',
                  //data: [this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt()]
                  data: this.temperature.reverse(),
                }, {
                  label: '습도',
                  backgroundColor: 'rgba(102,51,220,0.5)',
                  /*data: [this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt()]*/
                  data: this.humidity.reverse(),
                }, {
                  label: '토양온도',
                  backgroundColor: 'rgba(73,156,210,0.5)',
                  /*data: [this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt()]*/
                  data: this.soilTemperature.reverse(),
                }, {
                  label: '토양습도',
                  backgroundColor: 'rgba(51,220,144,0.5)',
                  /*data: [this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt()]*/
                  data: this.soilHumidity.reverse(),
                }, {
                  label: '조도',
                  backgroundColor: 'rgba(220,206,51,0.5)',
                  /*data: [this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt()]*/
                  data: this.lux.reverse(),
                }
              ]
            }
          });
    },
    getRealtimeIotdata() {
      this.$axios.get('/api/data/realtime?device=' + this.deviceId)
          .then(res => {
            this.iotdata = res.data;

            this.createdAt.push(this.timeFormatter(this.iotdata.createdAt).time);
            this.temperature.push(this.iotdata.temperature);
            this.humidity.push(this.iotdata.humidity);
            this.soilTemperature.push(this.iotdata.soilTemperature);
            this.soilHumidity.push(this.iotdata.soilHumidity);
            this.lux.push(this.iotdata.lux);

            if(this.createdAt.length > 20) {
              this.createdAt.shift();
              this.temperature.shift();
              this.humidity.shift();
              this.soilTemperature.shift();
              this.soilHumidity.shift();
              this.lux.shift();
            }

            this.dataCollection = {
              labels: this.createdAt,
              datasets: [
                {
                  label: '온도',
                  backgroundColor: 'rgba(248,121,121,0.5)',
                  //data: [this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt()]
                  data: this.temperature,
                }, {
                  label: '습도',
                  backgroundColor: 'rgba(102,51,220,0.5)',
                  /*data: [this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt()]*/
                  data: this.humidity,
                }, {
                  label: '토양온도',
                  backgroundColor: 'rgba(73,156,210,0.5)',
                  /*data: [this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt()]*/
                  data: this.soilTemperature,
                }, {
                  label: '토양습도',
                  backgroundColor: 'rgba(51,220,144,0.5)',
                  /*data: [this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt()]*/
                  data: this.soilHumidity,
                }, {
                  label: '조도',
                  backgroundColor: 'rgba(220,206,51,0.5)',
                  /*data: [this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt(), this.getRandomInt()]*/
                  data: this.lux,
                }
              ]
            }
          });
    },
    timeFormatter (timestamp) {
      timestamp = timestamp.split('T');
      let date = timestamp[0];
      let time = timestamp[1].split('.')[0];
      return {date, time};
    },

  }
}
</script>

<style scoped>
.small {
  max-width: 600px;
  margin:  50px auto;
}
</style>