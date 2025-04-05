
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


const eventsRef = ref(db, 'Events')

onValue(eventsRef,(snapshot) => {
  if (snapshot.exists()) {
    const data = snapshot.val()
    let eventList = document.querySelector(".event-box")
    //eventList.innerHTML = ``
    Object.entries(data).forEach(([id,event]) => {
       
      let eventDiv = document.createElement("div")
      eventDiv.classList.add("event")

      eventDiv.innerHTML = `
        <h1>${event.eventTitle}</h1>
        <p class="description">${event.description}</p>
        <div class="options">
          <div class="see-details">
            <p>See Details <span class="arrow">&rarr;</span></p>
          </div>
          <div class="rightb">
            <button class="guidelines">Important Guidelines</button>
            <button class="chat">Live Chat</button>
            <button class="feedback">Feedback</button>
          </div>
        </div>`
      const detailsButton = eventDiv.querySelector('.see-details')
      const chat = eventDiv.querySelector(".chat")
      const feedback = eventDiv.querySelector(".feedback")
      detailsButton.addEventListener('click', () => {
      
        window.location.href = `event.html?id=${id}`
      })

      chat.addEventListener('click', () => {
        window.location.href = `livechat.html?id=${id}`
      })

      feedback.addEventListener('click', () => {
        window.location.href = `` //feedback form link
      })

      //add that important guidelines button

      console.log(eventList)



    
      eventList.appendChild(eventDiv)
    })

  }
  else {
    console.log("No Events Found")
  }
  

})




