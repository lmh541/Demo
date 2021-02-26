$(document).ready(function(){
    // const select = document.getElementById("select");
    const input = document.getElementsByName("input[]");
    const output = document.getElementById("output");
    const images = document.getElementById("images");
    // const reset = document.getElementById("reset");
    let obj = {
        filename: [],
        image: [],
        size_img: []
    };

    const imgW = new Array();
    const imgH = new Array();

    // $("#reset").click(function() {               
    //     document.getElementById("select").value = "";
    //     inpI.remove();

    //     obj = new Object();
    //     obj = {
    //         filename: [],
    //         image: [],
    //         size_img: []
    //     };
    //     obj.no_img = 0;
    // })
    const swt = document.getElementById("switch");
    const check = $("input[type='checkbox']");

    check.click(function(){
        // $(".toggleSwitch").toggle();
        $(".rect").toggle(1000);
    });

    $("#submit").click(function(){
        $("#loading").show();
        $.ajax({
            type: 'post',
            url: "/predict",
            data : JSON.stringify(obj),
            dataType: 'json',
            processData: false,
            contentType: false,
            error: function(xhr, status, error){
                console.log(error);
                $("#loading").hide();
            },
            success: function(json){
                $("#loading").fadeOut("slow");
                swt.style.visibility = "visible"
                const coi = json.no_result;

                function imgWidth(num) {
                    const a = 500/num;
                    const b = a * num;
                    return b;
                }

                function imgHeight(num) {
                    const a = 312/num;
                    const b = a * num;
                    return b;
                }

                for(var i = 0; i < coi; i ++){
                    let idx = json.results[i].boxs[0];

                    for(var j = 0; j < json.results[i].boxs.length; j ++){
                        const rect = document.createElement("span");
                        let box = json.results[i].boxs[j];
                    
                        rect.className = "rect"
                        rect.style.position = "absolute";
                        rect.style.border = "solid red 1px";
                        rect.style.left = `${(500/imgW[i])*box[0]}px`;
                        rect.style.top = `${(312/imgH[i])*box[1]}px`;
                        rect.style.paddingRight = `${(box[2]-box[0])*(500/imgW[i])}px`;
                        rect.style.paddingBottom = `${(box[3]-box[1])*(312/imgH[i])}px`;
                        rect.title = box[4]*100 + "%";

                        let imgId = document.getElementById("bg");
                        imgId.appendChild(rect);
                    }
                }
            }
        })
    })
    
    $("#select").change(function(e){
        if(images.childNodes.length > 0) images.removeChild(images.childNodes[0]);
        if(check.is(":checked") == false) {
            check.prop("checked",true);
        }
        swt.style.visibility = "hidden";
        console.log(images.childNodes.length);
        const inpI = document.createElement("div");

        inpI.classList = "row center";
        inpI.style.paddingTop = "30px";
        inpI.style.justifyContent = "center";

        images.appendChild(inpI);

        const files = e.target.files;

        function readFile(index) {
            if(index >= files.length) {
                // console.log(JSON.stringify(obj))
                
                return;
            }

            obj.no_img = files.length;
            const fr = new FileReader();

            fr.readAsDataURL(files[index]);
            fr.onload = function(e) {
                if(images.childNodes)
                console.log(images.childNodes);

                const result = e.target.result;
                const bg = document.createElement("div");

                bg.id = "bg";
                bg.style.position = "relative";
                bg.style.background = "url(\'"+result+"\')";
                bg.style.backgroundRepeat = "no-repeat";
                bg.style.backgroundSize = "100% 100%";
                bg.style.width = `${500}px`;
                bg.style.height = `${320}px`;
                bg.style.borderRadius = "5px";
                bg.style.marginBottom = "10px";
                
                inpI.appendChild(bg);
                
                let img = new Image();
                
                img.src = result;
                img.onload = function() {

                    obj.filename.push(files[index].name);
                    obj.image.push(result.split(',')[1]);
                    obj.size_img.push(this.width * this.height);

                    imgW.push(this.width);
                    imgH.push(this.height);

                    readFile(index+1);
                }       
            }
        }
        readFile(0);
    })
})