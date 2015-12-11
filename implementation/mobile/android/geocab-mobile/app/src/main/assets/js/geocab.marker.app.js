
geocabapp.marker = function(){

	var templateAttribute = "<div><h2 class='marker-label'>{{label}}</h2><p class='marker-value'>{{value}}</p></div>";
	var templateImage = "<div><h2 class='marker-label'>{{label}}</h2><p class='marker-value-img'><img src='{{value}}'></p></div>";
    var templatePadding = "<div style='height: 50px; width: 100%'></div>";
	var currentMarker;
	var currentUser;
    var currentImage;
    var currentMotives;
	var assetsPath = "";
	
	/**
	 * Carrega os atributos do marker
	 */		
	var loadMarkerAttributes = function(element, marker, image){
		if ( typeof marker === 'string' )
			marker = toJSON(marker);
			
		// Atualiza os atributos do marker
		currentMarker.markerAttributes = marker.markerAttributes;
		
        // Verifica se foi cadastrado a layer
        if ( marker.layer !== undefined && marker.layer !== null)
			$(".marker-title", element).html(marker.layer.title);
		
		var html = "";
        html += templateAttribute.replace("{{label}}", marker.user.name).replace("{{value}}", formatTimestamp(marker.created));
		
		// Atributos
		for (j in marker.markerAttributes ) {
			var attr = marker.markerAttributes[j];
            var fieldValue = attr.value;
                            
            if ( attr.attribute.type == 'BOOLEAN' ){
                fieldValue = $("#"+attr.value).html();
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
		if ( !geocabapp.isEmpty(timestamp) ){
			timestamp = timestamp.toString().length == 13 ? parseInt(timestamp) : parseInt(timestamp) * 1000;
			var d = new Date(timestamp);
			return d.getDate() + '/' + (d.getMonth()+1) + '/' + d.getFullYear();		
		}
		return null;
    };
	
	var toJSON = function(value){
		value = value.replace(/(:\s*"[^"]*)"([^"]*"\s*(,|\s*\}))/g, '$1\\"$2');
		return JSON.parse(value);
	};
	
	/**
	 * Carrega as actions do marker
	 */		
	var loadMarkerActions = function(element, marker){
		if ( typeof marker === 'string' )
			marker = toJSON(marker);
        
        geocabapp.findMarker(currentMarker.id).status = marker.status;
        
		$(".marker-info-action").hide();
		$(".marker-action").hide();
		$(".marker-status").hide();
		
		if ((currentUser.role == 'ADMINISTRATOR' || currentUser.role == 'MODERATOR') || 
			(marker.status == 'PENDING' && currentUser.id == marker.user.id)){
            $(".marker-info-action").show();
			$(".marker-action.remove").show();
			$(".marker-status").show();
		}
		
		if ((currentUser.role == 'ADMINISTRATOR' || currentUser.role == 'MODERATOR') || 
			(marker.status == 'PENDING' && currentUser.id == marker.user.id)){
            $(".marker-info-action").show();
			$(".marker-status").show();
		}
		
		if ((currentUser.role == 'ADMINISTRATOR' || currentUser.role == 'MODERATOR') && 
			(marker.status == 'ACCEPTED' || marker.status == 'PENDING')){
            $(".marker-info-action").show();
			$(".marker-action.refuse").show();
		}
		
		if ((currentUser.role == 'ADMINISTRATOR' || currentUser.role == 'MODERATOR') && 
			(marker.status == 'REFUSED' || marker.status == 'PENDING')){
            $(".marker-info-action").show();
			$(".marker-action.approve").show();
		}
		
		if (marker.status != 'ACCEPTED' && currentUser.id == marker.user.id){
            $(".marker-action.update").show();
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
	var showInfoMarker = function(marker, image){
	
		$("#map").append($("#marker-template").html());
		var template = $("#map").children(".marker-info-box").last();
		
		loadMarkerAttributes(template, currentMarker, image);
		loadMarkerActions(template, currentMarker);
                              
        geocabapp.swipe.loadSwipeElement(template);
        geocabapp.swipe.animateInitialTop(template);
				
	};
    
    /**
     * Carrega o combo de motivos
     */
    var loadMotives = function(){
        
        $("#select-cause").empty();
        $("#select-cause").append("<option selected value='"+currentMotives[0].id+"'>"+currentMotives[0].name+"</option>");
        
        for (var i = 1; i < currentMotives.length; i++) {
            $("#select-cause").append("<option value='"+currentMotives[i].id+"'>"+currentMotives[i].name+"</option>");
        };
        
        $('#select-cause').selectmenu('refresh', true);
        
    };
    
	return {
		
		init : function(){
		
		},
		
		show : function(marker, image, loggedUser, layerProperties){
            
            $('#map').find('.marker-info-box').remove();
            geocabapp.swipe.setInfoBox(0);
            geocabapp.changeToActionState();
            geocabapp.getNativeInterface().showOpenMenuButton();
			
			if ( !geocabapp.isEmpty(layerProperties) ){
				layerProperties = JSON.parse(layerProperties);
				
				for ( k in layerProperties ){
					if ( layerProperties[k].features.length > 0 ){
						showInfoLayer(layerProperties[k], geocabapp.getLayersAdd()[k].title);
					}
				}
			}
			
			if ( !geocabapp.isEmpty(marker) && marker.id != 0){
				currentImage = image;
                currentMarker = typeof marker === 'string' ? toJSON(marker) : marker;
				currentUser = typeof loggedUser === 'string' ? JSON.parse(loggedUser) : loggedUser;
				$.extend(true, currentMarker, geocabapp.findMarker(currentMarker.id));
				
				showInfoMarker(currentMarker, image);
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

            currentImage = image;
			marker.user = geocabapp.findMarker(marker.id).user;
			loadMarkerAttributes(geocabapp.getElementFromMap(), marker, image);
		},
        
        loadMotives : function(motives){
            if ( typeof motives === 'string' )
                currentMotives = JSON.parse(motives);
            
            loadMotives();
        },
		
		hide : function(event){
            
            if ( geocabapp.isEmpty(event) ){
				geocabapp.swipe.destroyElement(geocabapp.getElementFromMap());
                
            } else {
				geocabapp.swipe.destroyElement(geocabapp.getElementFromEvent(event));
            }
            
		},
		
		update : function(){
			geocabapp.getNativeInterface().changeToUpdateMarker(JSON.stringify(currentMarker), currentImage);
		},	
		
		remove : function(){
			geocabapp.getNativeInterface().changeToRemoveMarker(JSON.stringify(currentMarker));
		},		
		
		approve : function(){
			geocabapp.getNativeInterface().changeToApproveMarker(JSON.stringify(currentMarker));
		},
		
		refuse : function(markerMotive){
			geocabapp.getNativeInterface().changeToRefuseMarker(JSON.stringify(currentMarker), JSON.stringify(markerMotive));
		}
	
	};

}();