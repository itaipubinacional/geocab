
geocabapp.marker = function(){

	var templateAttribute = "<div><h2 class='marker-label'>{{label}}</h2><p class='marker-value'>{{value}}</p></div>";
	var templateImage = "<div><h2 class='marker-label'>{{label}}</h2><p class='marker-value-img'><img src='{{value}}'></p></div>";
    var templatePadding = "<div style='height: 20px; width: 100%'></div>";
	var currentMarker;
	var currentUser;
	var assetsPath = "";
	
	/**
	 * Carrega os atributos do marker
	 */		
	var loadMarkerAttributes = function(element, marker, image){
		if ( typeof marker === 'string' )
			marker = JSON.parse(marker);
			
        // Verifica se foi cadastrado a layer
        if ( marker.layer !== undefined && marker.layer !== null)
			$(".marker-title", element).html(marker.layer.title);
		
		var html = "";
        
        html += templateAttribute.replace("{{label}}", marker.user.name)
        	.replace("{{value}}", formatTimestamp(marker.created));
		
		// Atributos
		for (j in marker.markerAttributes ) {
			var attr = marker.markerAttributes[j];
            var fieldValue = attr.value;
                            
            if ( attr.attribute.type == 'BOOLEAN' ){
                fieldValue = { Yes : 'Sim', No : 'N‹o' }[attr.value];
            }
            
            html += templateAttribute
                .replace("{{label}}", attr.attribute.name)
                .replace("{{value}}", fieldValue);
		}		

		// Imagem
		if ( image !== undefined && image !== "" ){
			html += templateImage.replace("{{label}}", "Imagem").replace("{{value}}", image);
		}
		
        html += templatePadding;
		$(".marker-info-content",element).html(html);
		
	};
	
    var formatTimestamp = function(timestamp){
		if ( timestamp !== undefined ){
			timestamp = timestamp.toString().length == 13 ? timestamp : timestamp * 1000;
			var d = new Date(timestamp);
			return d.getDate() + '/' + (d.getMonth()+1) + '/' + d.getFullYear();		
		}
		return null;
    };	
	
	/**
	 * Carrega as actions do marker
	 */		
	var loadMarkerActions = function(element, marker){
		if ( typeof marker === 'string' )
			marker = JSON.parse(marker);
			
		$(".marker-action").hide();
		$(".marker-status").hide();
		
		if ((currentUser.role == 'ADMINISTRATOR' || currentUser.role == 'MODERATOR') || 
			(marker.status == 'PENDING' && currentUser.id == marker.user.id)){
			$(".marker-action.remove").show();
			$(".marker-status").show();
		}
		
		if ((currentUser.role == 'ADMINISTRATOR' || currentUser.role == 'MODERATOR') || 
			(marker.status == 'PENDING' && currentUser.id == marker.user.id)){
			$(".marker-action.update").show();
			$(".marker-status").show();
		}
		
		if ((currentUser.role == 'ADMINISTRATOR' || currentUser.role == 'MODERATOR') && 
			(marker.status == 'ACCEPTED' || marker.status == 'PENDING')){
			$(".marker-action.refuse").show();
		}
		
		if ((currentUser.role == 'ADMINISTRATOR' || currentUser.role == 'MODERATOR') && 
			(marker.status == 'REFUSED' || marker.status == 'PENDING')){
			$(".marker-action.approve").show();
		}
		
		var status = { PENDING : 'STATUS PENDENTE', REFUSED : 'STATUS RECUSADO', ACCEPTED : 'STATUS APROVADO' };
		$(".marker-status", element).html(status[marker.status]);
	
	};
	
	/**
	 * Mostra a infobox da layer
	 */		
	var showInfoLayer = function(layerInfo, layerName){
		
		$("#map").append($("#layer-template").html());
		var template = $("#map").children(".marker-info-box").last();

   		geocabapp.swipe.hideElement(template);
		geocabapp.swipe.loadSwipeElement(template);	
		geocabapp.swipe.animateInitialTop(template);
		
		$(".marker-title", template).html(layerName);
		var html = "";
		
		for ( f in layerInfo.features ){
			for ( field in layerInfo.features[f].properties ){
                var value = layerInfo.features[f].properties[field];
				html += templateAttribute
					.replace("{{label}}", decodeURIComponent( escape(field)) )
					.replace("{{value}}", decodeURIComponent( escape(value)) );
			}			
		}
		
        html += templatePadding;
		$(".marker-info-content",template).html(html);
				
	};
	
	/**
	 * Mostra a infobox do marker
	 */			
	var showInfoMarker = function(marker){
	
		$("#map").append($("#marker-template").html());
		var template = $("#map").children(".marker-info-box").last();
		
		geocabapp.swipe.hideElement(template);
        geocabapp.swipe.loadSwipeElement(template);
		geocabapp.swipe.animateInitialTop(template);	
		
		loadMarkerAttributes(template, currentMarker, null);
		loadMarkerActions(template, currentMarker);
				
	};
    
	return {
		
		init : function(){
		
		},
		
		show : function(marker, image, loggedUser, layerProperties){
            
            $("#map").remove(".marker-info-box");
			
			if ( !geocabapp.isEmpty(layerProperties) ){
				layerProperties = JSON.parse(layerProperties);
				
				for ( k in layerProperties ){
					if ( layerProperties[k].features.length > 0 ){
						showInfoLayer(layerProperties[k], geocabapp.getLayersAdd()[k].title);
					}
				}
			}
			
			if ( !geocabapp.isEmpty(marker) ){
				currentMarker = typeof marker === 'string' ? JSON.parse(marker) : marker;
				currentUser = typeof loggedUser === 'string' ? JSON.parse(loggedUser) : loggedUser;
				$.extend(true, currentMarker, geocabapp.findMarker(currentMarker.id));
				
				showInfoMarker(currentMarker);
			}			
			
		},
		
        showOptions : function(markerId, attributes, image, userId, userRole, layerProperties){
            var marker = { id : markerId, markerAttributes : JSON.parse(attributes) };
            var user = { id : userId, role: userRole };
            this.show(marker, image, user, layerProperties);
        },		
		
		loadActions : function(marker){
			loadMarkerActions(geocabapp.getElementFromMap(), marker);
		},
		
		loadAttributes : function(marker, image){
            if ( typeof marker === 'string' )
                marker = JSON.parse(marker);
            
			marker.user = geocabapp.findMarker(marker.id).user;
			loadMarkerAttributes(geocabapp.getElementFromMap(), marker, image);
		},	
		
		hide : function(event){
            
            if ( geocabapp.isEmpty(event) ){
				geocabapp.swipe.destroyElement(geocabapp.getElementFromMap());
                
            } else {
				geocabapp.swipe.destroyElement(geocabapp.getElementFromEvent(event));
            }
            
		},
		
		update : function(){
			geocabapp.getNativeInterface().changeToUpdateMarker(JSON.stringify(currentMarker));
		},	
		
		remove : function(){
			geocabapp.getNativeInterface().changeToRemoveMarker(JSON.stringify(currentMarker));
		},		
		
		approve : function(){
			geocabapp.getNativeInterface().changeToApproveMarker(JSON.stringify(currentMarker));
		},
		
		refuse : function(){
			geocabapp.getNativeInterface().changeToRefuseMarker(JSON.stringify(currentMarker));
		}
	
	};

}();