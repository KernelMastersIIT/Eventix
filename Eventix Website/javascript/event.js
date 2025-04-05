
import { initializeApp } from "firebase/app"
import { ref,onValue,getDatabase } from "firebase/database"


const firebaseConfig = {
  apiKey: "AIzaSyBHWHTbfQ03Y7ZG99K5M-V9yerwJLYfotc",
  authDomain: "kernel-masters.firebaseapp.com",
  databaseURL: "https://kernel-masters-default-rtdb.firebaseio.com",
  projectId: "kernel-masters",
  storageBucket: "kernel-masters.firebasestorage.app",
  messagingSenderId: "111417793877",
  appId: "1:111417793877:web:448196f8474f088b4e63dc",
  measurementId: "G-JRJZQBYHFQ"
}


const app = initializeApp(firebaseConfig)
const db = getDatabase()

const url=new
URLSearchParams(window.location.search)
const eventId= url.get("id")

console.log(eventId)

const eventRef = ref(db,`Events/${eventId}`)

console.log(eventRef)

onValue(eventRef,(snapshot) => {

  console.log(snapshot)
  if(snapshot.exists()) {
    let data=snapshot.val()
    console.log(data)
    let eventInfo = document.querySelector(".event-info")

    let info = `
    <div class="details">
      <h1>${data.eventTitle} </h1>
      <p class="h">About the Event</p>
      <p class="jobdetails">${data.description}</p>
      

     
      
      <p class="h">Date</p>
      <p class="eventdetails">${data.date} </p>
      <p class="h">Timings</p>
      <p class="eventdetails">${data.time}</p>
      <p class="h">Location</p>
      <p class="eventdetails">${data.location} </p>
      

    </div>` 
    eventInfo.innerHTML += info

    console.log(eventInfo)

    const chatButton = document.querySelector(".chat-button")
    const fbButton = document.querySelector(".fb-button")
    chatButton.addEventListener('click', () => {
      window.location.href = `livechat.html?id=${eventId}`
    })
    

    fbButton.addEventListener('click', () => {
      window.location.href = `` //add the link here
    })

    

  }
})




 