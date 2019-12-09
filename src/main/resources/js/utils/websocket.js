import SockJS from 'sockjs-client'
import Stomp from 'webstomp-client'

var handlers = []
let stompClient = null

export function addHandler(handler) {
    handlers.push(handler)
}

export function sendVote(optionId, votingId) {
    if (stompClient && stompClient.connected) {
        stompClient.send('/app/voting-websocket/' + votingId, optionId, {})
    }
}

export function connectWebsocket(votingId) {
    const socket = new SockJS('/voting-websocket')
    stompClient = Stomp.over(socket)
    stompClient.connect({}, (frame) => {
        stompClient.subscribe('/topic/voting/' + votingId, data => {
            handlers.forEach(handler => handler(JSON.parse(data.body)))
        })
    }, error => {
        console.log('disconnected! Reason: ' + error.body)
    })
}

export function disconnectWebsocket() {
    if (stompClient && stompClient.connected) {
        stompClient.disconnect()
    }
}