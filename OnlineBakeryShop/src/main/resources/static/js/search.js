


const search = () => {
    console.log("searching................");
    let word = $("#search-input").val();
    if(word == ''){
        $(".search-result").hide();
    }else{
        //console.log(word);

        let url = `http://localhost:9090/search/${word}`;
        fetch(url)
        .then((response) => {
            return response.json();
        })
        .then((searchList) => {
            //console.log(searchList);
            let text = `<div class='list-group'>`;
            searchList.forEach((Products) =>{
                text+=`<a href='/bakeryshop/shop/viewDetails/${Products.pid}' class='list-group-list list-group-action'>${Products.name}</a>`
            });

            text+=`</div>`;
            $(".search-result").html(text);
            $(".search-result").show();
        })
        
    }
}