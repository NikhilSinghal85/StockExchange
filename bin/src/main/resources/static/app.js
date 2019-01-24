var stompClient = null;
var sub = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#stocks").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        this.sub = stompClient.subscribe('/topic/stocks', function (stocks) {
            showStocks(JSON.parse(stocks.body).content);
        });
    });
}

function disconnect() {
	if (this.sub !== null) {
        this.sub.unsubscribe();
        this.sub = null;
    }
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/stock", {}, JSON.stringify({'name': $("#name").val(), 'value': $("#value").val() }));
}

function showStocks(message) {
	if (message.includes("NSE")) {
		$("#stocks").empty();
    	$("#stocks").append("<tr><td> " + message + "</td></tr>");
    }
    else {
    	$("#stocks2").empty();
    	$("#stocks2").append("<tr><td> &emsp;" + message + "</td></tr>");
    }
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});

connect();