
geocabapp.marker = function(){

	var templateAttribute = "<div><h2 class='marker-label'>{{label}}</h2><p class='marker-value'>{{value}}</p></div>";
	var templateImage = "<div><h2 class='marker-label'>{{label}}</h2><p class='marker-value-img'><img src='{{value}}'></p></div>";
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
                fieldValue = { Yes : 'Sim', No : 'Não' }[attr.value];
            }
            
            html += templateAttribute
                .replace("{{label}}", attr.attribute.name)
                .replace("{{value}}", fieldValue);
		}		

		// Imagem
		if ( image !== undefined && image !== "" ){
			html += templateImage.replace("{{label}}", "Imagem").replace("{{value}}", image);
		}
		
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
		
		$("#info").append($("#layer-template").html());
		var template = $("#info :last-child");
		
		geocabapp.swipe.loadSwipeElement(template);	
		geocabapp.swipe.hideElement(template);
		geocabapp.swipe.animateInitialTop(template);
		
		$(".marker-title", template).html(layerName);
		var html = "";
		
		for ( f in layerInfo.features ){
			for ( field in layerInfo.features[f].properties ){
				html += templateAttribute
					.replace("{{label}}", field)
					.replace("{{value}}", layerInfo.features[f].properties[field]);		
			}			
		}
		
		$(".marker-info-content",template).html(html);
				
	};
	
	/**
	 * Mostra a infobox do marker
	 */			
	var showInfoMarker = function(marker){
	
		$("#info").append($("#marker-template").html());
		var template = $("#info :last-child");
		
		geocabapp.swipe.loadSwipeElement(template);
		geocabapp.swipe.hideElement(template);
		geocabapp.swipe.animateInitialTop(template);	
		
		loadMarkerAttributes(template, currentMarker, null);
		loadMarkerActions(template, currentMarker);
				
	};
	
	return {
		
		init : function(){
		
		},
		
		show : function(marker, image, loggedUser, layerProperties){
			var clean = false;
			
			if ( layerProperties !== null && layerProperties !== "" ){
				layerProperties = layerProperties.replace('\\"', '"');
				layerProperties = layerProperties.replace('"{', '{');
				layerProperties = layerProperties.replace('}"', '}');		
				layerProperties = layerProperties.replace('}"', '}');
				layerProperties = JSON.parse(layerProperties);
				
				for ( k in layerProperties ){
					if ( layerProperties[k].features.length > 0 ){
						$("#info").empty();
						clean = true;
						showInfoLayer(layerProperties[k], geocabapp.getLayersAdd()[k].title);
					}
				}
			}
			
			if ( marker !== null && marker !== "" ){
				if ( !clean) 
					$("#info").empty();
					
				currentMarker = typeof marker === 'string' ? JSON.parse(marker) : marker;
				currentUser = typeof loggedUser === 'string' ? JSON.parse(loggedUser) : loggedUser;
				$.extend(true, currentMarker, geocabapp.findMarker(currentMarker.id));
				
				showInfoMarker(currentMarker);
			}			
			
			geocabapp.hideStates();
		},
		
        showOptions : function(markerId, attributes, image, userId, userRole){
            var marker = { id : markerId, markerAttributes : JSON.parse(attributes) };
            var user = { id : userId, role: userRole };
            this.show(marker, image, user);
        },		
		
		loadActions : function(marker){
			loadMarkerActions(marker);
		},
		
		loadAttributes : function(marker, image){
            if ( typeof marker === 'string' )
                marker = JSON.parse(marker);
            
			marker.user = geocabapp.findMarker(marker.id).user;
			loadMarkerAttributes(marker, image);
		},	
		
		hide : function(event){
			geocabapp.swipe.destroyElement(geocabapp.getElementFromEvent(event));
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