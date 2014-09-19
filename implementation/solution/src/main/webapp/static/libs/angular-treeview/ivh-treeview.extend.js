angular.module("ivh.treeview-extend",["ivh.treeview"]).directive("ivhTreeview", function(){

    return {
        link: function (scope, element, attrs) {

            scope.$on('event_ivhTreeviewSelectEnd', function(event, isSelected) {

                var item = event.currentScope.itm;

                    scope.$emit('$event_ivhTreeviewSelectItem', item);

            });
        }
    }
})
.directive('ivhFn',function(){
    return {
        link: function(scope, element, attrs){
            scope.$on('$event_ivhTreeviewSelectItem', function(event, item) {
                scope.fn = scope.$eval(attrs.ivhFn);
                scope.fn(item);
            });
        }
    }
})