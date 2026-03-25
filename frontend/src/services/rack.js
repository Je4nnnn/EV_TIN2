import httpClient from "../http-common";

// obetenr todos los rack

const getAllRacks = () => {
  return httpClient.get("/racks/all");
};

export default {getAllRacks};