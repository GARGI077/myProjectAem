$(document).ready(function() {
        console.log("hello")
     var $searchResultContainer = $('.search-results__items'),
                searchResultsUrl = $('.search__form-container').attr('data-nodepath') + '.searchresults.json',
                $submit = $('.search-btn'),
                $searchResultContainer = $('.search-results__items');
    $submit.on('click', function(event) {
        event.preventDefault();
        _getSearchResults($.trim($('#searchTerm').val()));
    });
    function _resultsTemplate(item) {
        var title = item.title,
         pageUrl = item.pageUrl;

        var resultTemplate = '<a class="search-item" href="' + pageUrl  + '">' +
            '<p class="search-item__heading">' + title + '</p>' +
            '</a>'
        return resultTemplate;
    }
    function _getSearchResults(searchValue) {
        $searchResultContainer.empty();
        var searchPath = $('.searchPath').val();

        if (searchValue.length > 0) {
            $.ajax({
                url: searchResultsUrl,
                type: "GET",
                data: {
                    searchTerm: searchValue,
                    searchPath: searchPath,
                },
                success: function(data) {
                        console.log(data)
                    var searchResult = $(document.createDocumentFragment());
                    var searchDetails;
                    if (data.length > 0) {
                        var searchResultData = data;
                        var searchDetails;
                        $(searchResultData).each(function(index, item) {
                            searchDetails = _resultsTemplate(item);
                            searchResult.append(searchDetails);
                        });
                    } else {
                        searchDetails = '<p> No Results Found</p>'
                        searchResult.append(searchDetails);
                    }
                    $searchResultContainer.append(searchResult);
                },
                error: function(data) {
                    console.log("error");
                }
            });
        }
    }
});
