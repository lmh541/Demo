$(document).ready(function() {
    $("#btnup").click(function(e){
        e.preventDefault();
        $("#select").click();
    })
    $('#select').change(function(e){
        //here we take the file extension and set an array of valid extensions
        var res=$('#select').val();
        var arr = res.split("\\");
        var filename=arr.slice(-1)[0];
        var names = new Array();

        let filextension=filename.split(".");
        let filext="."+filextension.slice(-1)[0];
        let valid=[".jpg",".png",".jpeg",".bmp"];
        //if file is not valid we show the error icon, the red alert, and hide the submit button
        if (valid.indexOf(filext.toLowerCase())==-1){
            $( ".imgupload" ).hide("slow");
            $( ".imgupload.ok" ).hide("slow");
            $( ".imgupload.stop" ).show("slow");
            $( "#images").hide("slow");
        
            $('#namefile').css({"color":"red","font-weight":700});
            $('#namefile').html("File "+filename+" is not  pic!");
            
            $( "#submit" ).hide();
            $( "#fakebtn" ).show();
        }else{
            //if file is valid we show the green alert and show the valid submit
            $( ".imgupload" ).hide("slow");
            $( ".imgupload.stop" ).hide("slow");
            $( ".imgupload.ok" ).show("slow");
        
            $('#namefile').css({"color":"green","font-weight":700,"font-size":"20px"});
            // for(var i = 0; i < e.target.files.length; i ++)
            //     names.push(e.target.files[i].name);
            // $('#namefile').html(JSON.stringify(names));
            $('#namefile').html(filename);
        
            $( "#submit" ).show();
            $( "#fakebtn" ).hide();

            $("#submit").click(function(e) {
                $( ".imgupload" ).hide("slow");
                $( ".imgupload.stop" ).hide("slow");
                $( ".imgupload.ok" ).hide("slow");
            });
        }
    });
})