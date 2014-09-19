var timeOut;
$(document).ready(function(){
    timeOut = $.timeoutDialog();

    function useLoadingMessage(message) {
        var loadingMessage;
        if (message) loadingMessage = message;
        else loadingMessage = "Loading";

        var disabledZone = document.createElement('div');
        disabledZone.setAttribute('id', 'disabledZone');
        disabledZone.style.position = "absolute";
        disabledZone.style.zIndex = "1000";
        disabledZone.style.left = "0px";
        disabledZone.style.top = "0px";
        disabledZone.style.width = "100%";
        disabledZone.style.height = "100%";
        document.body.appendChild(disabledZone);
        var messageZone = document.createElement('div');
        messageZone.setAttribute('id', 'messageZone');
        messageZone.style.position = "absolute";
        messageZone.style.top = "0px";
        messageZone.style.right = "0px";
        messageZone.style.background = "red";
        messageZone.style.color = "white";
        messageZone.style.fontFamily = "Arial,Helvetica,sans-serif";
        messageZone.style.padding = "4px";
        disabledZone.appendChild(messageZone);
        var text = document.createTextNode(loadingMessage);
        messageZone.appendChild(text);
        disabledZone.style.visibility = 'hidden';

        dwr.engine.setPreHook(function() {
            disabledZone.style.visibility = 'visible';
        });

        dwr.engine.setPostHook(function() {
            timeOut.keepAliveFromRequest();
            disabledZone.style.visibility = 'hidden';
        });
    }

    useLoadingMessage("Aguarde.");
});