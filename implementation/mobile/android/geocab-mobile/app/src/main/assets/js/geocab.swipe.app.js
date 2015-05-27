
geocabapp.swipe = function(){

	var infoBoxNumber = 0;
	var HEADER_HEIGHT = 65;
	var MAX_INFO_BOX = 10;
	
	/**
	 * Captura os eventos do touch
	 */
	var swipeStatus = function(event, phase, direction, distance) {
		console.log("Distancia " + distance + " Direction " + direction + " Phase " + phase);
		
		var element = geocabapp.getElementFromEvent(event);
		var duration = 0;
		var speed = 500;
		
		// Se esta em movimento e a direção é pra cima ou para baixo
		if (phase == "move" && direction == "up") {
			scrollToPosition(element, element.data("initial-top") - distance, duration);
		} else if (phase == "move" && direction == "down") {
			scrollToPosition(element, distance, duration);
		
		// Se foi cancelado o movimento e a direção é pra cima ou para baixo
		} else if (phase == "cancel" && direction == "up") {
			scrollToPosition(element, element.data("initial-top"), speed);
		} else if (phase == "cancel" && direction == "down") {
			scrollToPosition(element, 0, speed);
			
		// Se o evento terminou e a direção é pra cima ou para baixo
		} else if (phase == "end" && direction == "up") {
			scrollToPosition(element, 0, speed);
		} else if (phase == "end" && direction == "down") {
			scrollToPosition(element, element.data("initial-top"), speed);
		}
	}
	
	var swipeOptions = {
		triggerOnTouchEnd: true,
		swipeStatus: swipeStatus,
		allowPageScroll: "none",
		threshold: 150
	};
	
	/**
	 * Atualiza a posição do painel
	 */
	var scrollToPosition = function(element, distance, duration) {
		$(element).css("transition-duration", (duration / 1000).toFixed(1) + "s");
		$(element).css("top", distance);
	}
	
	var animateInitialTop = function(element){
		scrollToPosition(element, element.data("initial-top"), 500);
	};
	
	var loadSwipeElement = function(element){
		$(".marker-info-header", element).swipe(swipeOptions);
	};
	
	var hideElement = function(element){
		var parentHeight = $("#map").height();
		element.css("top", parentHeight);
	};
	
	var destroyElement = function(element){
		// Move a posicao de todos os boxes proximos
		element.nextAll().each(function(i, infobox){
			var newTop = $("#map").height() - HEADER_HEIGHT;
			$(infobox).css("top", newTop);
		});
		
		// Esconde o infobox atual
		hideElement(element);
		setInterval(function(){  element.remove()  }, 500);
		
		// Retorna para o state padrao
		infoBoxNumber--;		
		if ( infoBoxNumber == 0 ){
			geocabapp.changeToActionState();
			//geocabapp.getNativeInterface().showOpenMenuButton();
		}
	};	
	
	return {
		
		init : function(el){

		},
		
		hideElement : function(element){
			hideElement(element);
		},
		
		destroyElement : function(element){
			destroyElement(element);
		},		
		
		animateInitialTop : function(element){
			animateInitialTop(element);
		},
		
		loadSwipeElement : function(element){
			infoBoxNumber++;
			element.data("info-box-number", infoBoxNumber);
			element.data("initial-top", element.parent().height() - (infoBoxNumber * HEADER_HEIGHT));
			element.css("z-index", MAX_INFO_BOX - infoBoxNumber);
			loadSwipeElement(element);
		}
	
	};

}();