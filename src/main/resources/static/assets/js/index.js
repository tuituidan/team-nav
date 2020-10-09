(function($){
  $(function(){

    $('.button-collapse').sideNav();
    const $searchList = $(".card-row");
    for (let i = 1; i < 19; i++) {
      $searchList.append(template('itemHtml', {index: i}));
    }
  }); // end of document ready
})(jQuery); // end of jQuery name space
