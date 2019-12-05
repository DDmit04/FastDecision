import { Stomp } from '@stomp/stompjs'
import SockJS from 'sockjs-client'

let stompClient = null

export function connect(onConnected, onError) {
    const socket = new SockJS('/voting-websocket')
    stompClient = Stomp.over(socket)
    stompClient.debug = () => {}
    stompClient.connect({}, onConnected, onError)
    return stompClient
}