<script>
import {Line, mixins} from "vue-chartjs";
import { locale } from 'moment';

export default {
  name: "line-chart",
  extends: Line,
  mixins: [mixins.reactiveProp],
  props: ["chartData", "options"],
  mounted() {
    this.renderChart(this.chartData, this.options);
  },
  created() {
    locale("ko");
  },
  watch: {
    chartOptions() {
      this.renderChart(this.chartData, this.options);
    }
  }
}
</script>

<style scoped>

</style>