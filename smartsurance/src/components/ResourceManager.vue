<template>
  <div id="resourceManager">
    <div id="cpuTime" class="mb-4">
      CPU Time (us):
      <b-form-input
        v-model="cputime"
        class="d-inline"
        type="number"
        required
      ></b-form-input
      ><b-button
        v-on:click="setCPUTime()"
        class="d-block w-100 mt-2"
        variant="success"
        >Apply</b-button
      >
    </div>

    <hr />

    <div id="memoryHigh" class="mb-4">
      Memory High (bytes):
      <b-form-input
        v-model="memoryhigh"
        class="d-inline"
        type="number"
        required
      ></b-form-input
      ><b-button
        v-on:click="setMemoryHigh()"
        class="d-block w-100 mt-2"
        variant="success"
        >Apply</b-button
      >
    </div>

    <hr />

    <div id="memoryMax" class="mb-4">
      Memory Max (bytes):
      <b-form-input
        v-model="memorymax"
        class="d-inline"
        type="number"
        required
      ></b-form-input
      ><b-button
        v-on:click="setMemoryMax()"
        class="d-block w-100 mt-2"
        variant="success"
        >Apply</b-button
      >
    </div>

    <hr />

    <div id="diskRead" class="mb-4">
      Disk Read (bytes):
      <b-form-input
        v-model="diskread"
        class="d-inline"
        type="number"
        required
      ></b-form-input
      ><b-button
        v-on:click="setDiskRead()"
        class="d-block w-100 mt-2"
        variant="success"
        >Apply</b-button
      >
    </div>

    <hr />

    <div id="diskWrite" class="mb-4">
      Disk Write (bytes):
      <b-form-input
        v-model="diskwrite"
        class="d-inline"
        type="number"
        required
      ></b-form-input
      ><b-button
        v-on:click="setDiskWrite()"
        class="d-block w-100 mt-2"
        variant="success"
        >Apply</b-button
      >
    </div>
  </div>
</template>

<script>
import config from "@/config";
import urljoin from "url-join";

export default {
  name: "ResourceManager",
  data() {
    return {
      endpoint: config.resourceManager,
      cputime: 25000,
      memoryhigh: 128 * 1024 * 1024,
      memorymax: 192 * 1024 * 1024,
      diskread: 10 * 1024 * 1024,
      diskwrite: 5 * 1024 * 1024,
    };
  },
  props: {},
  methods: {
    async setCPUTime() {
      try {
        await this.$http.post(urljoin(this.endpoint, "/cpu/time"), {
          time: this.cputime,
        });
        this.$toast.success("Successfully set CPU time.");
      } catch (error) {
        this.$toast.error(
          "Setting resource manager property failed with:\n" + error
        );
      }
    },
    async setMemoryHigh() {
      try {
        await this.$http.post(urljoin(this.endpoint, "/memory/high"), {
          memoryHigh: this.memoryhigh,
        });
        this.$toast.success("Successfully set memory high value.");
      } catch (error) {
        this.$toast.error(
          "Setting resource manager property failed with:\n" + error
        );
      }
    },
    async setMemoryMax() {
      try {
        await this.$http.post(urljoin(this.endpoint, "/memory/max"), {
          memoryMax: this.memorymax,
        });
        this.$toast.success("Successfully set memory max value.");
      } catch (error) {
        this.$toast.error(
          "Setting resource manager property failed with:\n" + error
        );
      }
    },
    async setDiskRead() {
      try {
        await this.$http.post(urljoin(this.endpoint, "/io/rbps"), {
          deviceMaj: config.deviceMaj,
          deviceMin: config.deviceMin,
          rbps: this.diskread,
        });
        this.$toast.success("Successfully set disk read speed.");
      } catch (error) {
        this.$toast.error(
          "Setting resource manager property failed with:\n" + error
        );
      }
    },
    async setDiskWrite() {
      try {
        await this.$http.post(urljoin(this.endpoint, "/io/wbps"), {
          deviceMaj: config.deviceMaj,
          deviceMin: config.deviceMin,
          wbps: this.diskwrite,
        });
        this.$toast.success("Successfully set disk write speed.");
      } catch (error) {
        this.$toast.error(
          "Setting resource manager property failed with:\n" + error
        );
      }
    },
  },
  watch: {},
};
</script>

<style scoped>
</style>
