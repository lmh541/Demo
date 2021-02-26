let i = 0;

export const urlToPromise = function(url) {
    return new Promise(function(resolve, reject) {
        JSZipUtils.getBinaryContent(url, function (err, data) {
            if(err) {
                reject(err);
            } else {
                resolve(data);
            }
        });
    });
}

export const checkAll = function($checkboxArr){
    if($("#checkboxAll").is(":checked")){
        $($checkboxArr).prop("checked", true);
    }else{
        $($checkboxArr).prop("checked", false);
    }
}