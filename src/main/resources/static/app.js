var stompClient = null;
var sub = null;
var sub2 = null;
var sub3 = null;

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
        sendName();
        this.sub = stompClient.subscribe('/topic/stocks', function (stocks) {
            showStocks(stocks.body);
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
		$("#stocks").empty();
    	$("#stocks").append("<tr><td>NSE Idea  :: " +JSON.parse(message).nse_Idea + "</td></tr>");
    	$("#stocks2").empty();
    	$("#stocks2").append("<tr><td>NSE Tata  :: " + JSON.parse(message).nse_Tata + "</td></tr>");
    	$("#stocks3").empty();
    	$("#stocks3").append("<tr><td>NSE Coal India  :: " + JSON.parse(message).nse_CoalIndia + "</td></tr>");
    	$("#stocks4").empty();
    	$("#stocks4").append("<tr><td>BSE Idea  :: " + JSON.parse(message).bse_Idea + "</td></tr>");
    	$("#stocks5").empty();
    	$("#stocks5").append("<tr><td>BSE Tata  :: " + JSON.parse(message).bse_Tata + "</td></tr>");
    	$("#stocks6").empty();
    	$("#stocks6").append("<tr><td>BSE Coal India  :: " + JSON.parse(message).bse_CoalIndia + "</td></tr>");
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