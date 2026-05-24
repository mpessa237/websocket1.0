let stompClient = null;
let username = null;


function connect() {
    username = document.getElementById('username-input').value.trim();

    if (!username) {
        alert('Entre ton prénom pour rejoindre !');
        return;
    }


    const socket = new SockJS('/ws');


    stompClient = Stomp.over(socket);

    stompClient.debug = null;


    stompClient.connect({}, onConnected, onError);
}


function onConnected() {


    stompClient.subscribe('/topic/messages', onMessageReceived);


    stompClient.send(
        '/app/chat.addUser',
        {},
        JSON.stringify({
            sender: username,
            content: username + ' a rejoint le chat',
            type: 'JOIN'
        })
    );

    document.getElementById('login-screen').classList.add('hidden');
    document.getElementById('chat-screen').classList.remove('hidden');
    document.getElementById('connected-user').textContent =
        'Connecté en tant que : ' + username;
}


function onError(error) {
    console.error('Erreur de connexion WebSocket :', error);
    alert('Impossible de se connecter au serveur. Vérifie que Spring Boot est démarré.');
}


function sendMessage() {
    const content = document.getElementById('message-input').value.trim();

    if (content && stompClient && stompClient.connected) {
        stompClient.send(
            '/app/chat',
            {},
            JSON.stringify({
                sender: username,
                content: content,
                type: 'CHAT'
            })
        );
        document.getElementById('message-input').value = '';
    }
}


function onMessageReceived(payload) {

    const message = JSON.parse(payload.body);

    const li = document.createElement('li');

    if (message.type === 'JOIN') {
        li.classList.add('system-message');
        li.textContent = message.content;

    } else if (message.type === 'LEAVE') {
        li.classList.add('system-message');
        li.textContent = message.sender + ' a quitté le chat';

    } else {
        li.classList.add(message.sender === username ? 'my-message' : 'other-message');
        li.innerHTML = '<strong>' + message.sender + '</strong> : ' + message.content;
    }

    document.getElementById('messages-list').appendChild(li);

    li.scrollIntoView({ behavior: 'smooth' });
}


document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('message-input')
        .addEventListener('keypress', function (e) {
            if (e.key === 'Enter') sendMessage();
        });

    document.getElementById('username-input')
        .addEventListener('keypress', function (e) {
            if (e.key === 'Enter') connect();
        });
});