import axios from "axios";

// axios 객체 생성
export default axios.create({
  baseURL: "http://j6b207.p.ssafy.io:9090/",
  headers: {
    "Content-type": "application/json",
  },
});
