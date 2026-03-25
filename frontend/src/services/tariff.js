import httpClient from "../http-common";

const getAll = () => { 
    return httpClient.get("/tariffs/all");
}

export default { getAll };