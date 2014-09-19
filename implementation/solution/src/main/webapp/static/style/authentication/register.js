$(document).ready(function(){
	
	$("#register").click(function(event){
		event.preventDefault();
		$(".register-overlay").fadeToggle("fast");
	});
	
	$(".enter").click(function(event){
		event.preventDefault();
		$(".register-overlay").fadeToggle("fast");
	});
	
	$(".close").click(function(event){
		event.preventDefault();
		$(".register-overlay").fadeToggle("fast");
	});

	$(document).keyup(function(e) {
		if(e.keyCode == 27 && $(".register-overlay").css("display") != "none" ) { 
			event.preventDefault();
			$(".register-overlay").fadeToggle("fast");
		}
	});
	
});