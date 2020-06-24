import SockJS from 'sockjs-client'
import Stomp from 'webstomp-client'

var handlers = []
var errorHandlers = []
let stompClient = null

export function addHandler(handler) {
    handlers.push(handler)
}

export function addErrorHandler(handler) {
    errorHandlers.push(handler)
}

export async function sendVote(optionId, votingId, votingKey) {
    if (stompClient && stompClient.connected) {
        await stompClient.send('/app/voting-websocket/' + votingId + '/' + votingKey, optionId, {})
        return true
    } else {
        return false
    }
}

export function connectWebsocket(votingId, votingKey) {
    const socket = new SockJS('/voting-websocket')
    stompClient = Stomp.over(socket)
    stompClient.connect({}, (frame) => {
        stompClient.subscribe('/topic/voting/' + votingId + '/' + votingKey, data => {
            handlers.forEach(handler => handler(JSON.parse(data.body)))
        })
    }, error => {
        errorHandlers.forEach(handler => handler(error.body))
    })
}

export function disconnectWebsocket() {
    if (stompClient && stompClient.connected) {
        console.log("DISCONNECTED")
        stompClient.disconnect()
    }
}