$(document).ready(function () {
  const thumbnailBody = document.getElementById("thumbnailBody");
  const thumbnailCon = document.getElementById("thumbnailCon");
  const resultImg = document.getElementById("resultImg");
  const popupImg = document.createElement("canvas");
  const typeTxt = document.createElement("p");
  const jsonStr = document.getElementById("jsonStr");

  var $panel = $(".popup_panel");
  var $panelContents = $(".popup_contents");
  var $loading = $("#loading");

  let version = 'AI Version: ';

  let obj = {
    filename: [],
    image: [],
    size_img: [],
    no_img: 0
  };

  let bgJsonData = {
    "img_name": [],
    "no_result": 0,
    "results": [],
  }

  const imgW = new Array();
  const imgH = new Array();

  function clearNodes(parentNode) {
    if (parentNode.childNodes.length > 0)
      while (parentNode.hasChildNodes()) parentNode.removeChild(parentNode.firstChild);
  }

  $("#btn_popup_close").on("click", popupClose);
  // 팝업 배경 클릭 이벤트 정의
  $panel.find(".popup_bg").on("click", popupClose);
  function popupClose(e) {
    $panel.fadeOut();
    // 이벤트 기본 동작 중단
    e.preventDefault();
  }

  $("#select").change(function (e) {
    clearNodes(thumbnailCon);
    const files = e.target.files;
    let idx = 0;

    thumbnailBody.style.display = "flex";

    obj = {
      filename: [],
      image: [],
      size_img: [],
      no_img: 0
    };
    imgW.splice(0, files.length);
    imgH.splice(0, files.length);

    function readFile(index) {
      $loading.show();
      $("#subtitle").css("display", "block");
      if (index >= files.length) {
        $("#btnup").css("display", "none");
        $("#useage").css("display", "none");
        $("#version").css("display", "none");
        $("#refresh").css("display", "inline-block");
        $("#refresh").click(() => {
          $("#select").prop("value", "");
          clearNodes(thumbnailCon);
          bgJsonData = {
            "img_name": [],
            "no_result": 0,
            "results": [],
          }
          $("#subtitle").css("display", "none");
          $("#thumbnailBody").css("display", "none");
          $("#jsonStr").css("display", "none");
          $("#refresh").css("display", "none");
          $("#btnup").css("display", "inline-block");
          $("#useage").css("display", "block");
          $("#version").css("display", "block");

          obj = {
            filename: [],
            image: [],
            size_img: [],
            no_img: 0
          };
          imgW.splice(0, files.length);
          imgH.splice(0, files.length);
        });

        $.ajax({
          type: 'post',
          url: "http://divus.iptime.org:4207/vin/ai-api/ver-0.1.10",
          data: JSON.stringify(obj),
          dataType: 'json',
          processData: false,
          contentType: false,
          beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"));
          },
          error: function (xhr, status, error) {
            $loading.hide();
            console.log(xhr);
            alert("에러가 발생했습니다..");
          },
          success: function (json) {
            $loading.fadeOut("slow");
            $("#jsonStr").css("display", "block");
            version += json.ai_version + '\n';
            for (var i = 0; i < json.no_result; i++) {
              bgJsonData.img_name.push(obj.filename[i]);
              bgJsonData.no_result = json.no_result;
              bgJsonData.results.push(json.results[i]);
            }
            jsonStr.innerHTML = version;
            jsonStr.innerHTML += JSON.stringify(bgJsonData, null, '\t');
            version = "AI Version: ";
          }
        })
        return;
      }

      const fr = new FileReader();
      fr.readAsDataURL(files[index]);
      fr.onload = function (eve) {
        const result = eve.target.result;
        const bg = document.createElement("div");

        bg.id = idx;
        bg.style.float = "left";
        bg.style.position = "relative";
        bg.style.background = "url(\'" + result + "\')";
        bg.style.backgroundRepeat = "no-repeat";
        bg.style.backgroundSize = "cover";
        bg.style.width = `${100}px`;
        bg.style.height = `${100}px`;
        bg.style.borderRadius = "5px";
        bg.style.marginTop = "10px";
        bg.style.marginLeft = "10px";
        bg.style.marginBottom = "10px";
        bg.addEventListener("mouseover", function () {
          bg.setAttribute('class', 'hover');
        });
        bg.addEventListener("mouseout", function () {
          bg.setAttribute('class', 'mouseout');
        })
        bg.addEventListener("click", function () {
          let imgIdx = Number(bg.id);
          //$loading.show();
          clearNodes(resultImg);

          var jsonData = "{ " + "\"filename\": [ \"" + obj.filename[imgIdx] + "\"]";
          jsonData += ",\"image\" : [\"" + obj.image[imgIdx] + "\"]";
          jsonData += ",\"size_img\" : [ [" + imgW[imgIdx] + "," + imgH[imgIdx] + "] ]";
          jsonData += ",\"no_img\" : 1";
          jsonData += "}";

          // 팝업 가운데 설정(가로)
          if ($panelContents.outerWidth() < $(document).width()) {
            $panelContents.css("margin-left", "-" + $panelContents.outerWidth() / 2 + "px");
          } else {
            $panelContents.css("left", "0px");
          }

          // 팝업 가운데 설정(세로)
          if ($panelContents.outerHeight() < $(document).height()) {
            $panelContents.css("margin-top", "-" + $panelContents.outerHeight() / 2 + "px");
          } else {
            $panelContents.css("top", "0px");
          }

          clearNodes(typeTxt);
          clearNodes(popupImg);

          $(typeTxt).css({
            position: "absolute",
            bottom: "-3px",
            right: "3px",
            margin: 0,
            color: "red"
          })

          $(popupImg).css({
            borderRadius: "5px",
            margin: "0px",
          });

          var ctx = popupImg.getContext('2d');
          var imgObj = new Image();
          imgObj.onload = function () {
            popupImg.width = 500;
            popupImg.height = 312;
            ctx.drawImage(imgObj, 0, 0, 500, 312);
            if (bgJsonData.results[imgIdx] !== undefined) {
              let box = bgJsonData.results[imgIdx].boxs[0];
              typeTxt.innerHTML = `${box[1]} / ${parseFloat(box[2] * 100).toFixed(2) + '%'}`;

              // ctx.strokeStyle = "red";

              // ctx.beginPath();

              // ctx.moveTo((500 / imgW[imgIdx]) * box[0][0][0], (312 / imgH[imgIdx]) * box[0][0][1]);
              // ctx.lineTo((500 / imgW[imgIdx]) * box[0][0][2], (312 / imgH[imgIdx]) * box[0][0][1]);

              // ctx.moveTo((500 / imgW[imgIdx]) * box[0][0][2], (312 / imgH[imgIdx]) * box[0][0][1]);
              // ctx.lineTo((500 / imgW[imgIdx]) * box[0][0][2], (312 / imgH[imgIdx]) * box[0][0][3]);

              // ctx.moveTo((500 / imgW[imgIdx]) * box[0][0][0], (312 / imgH[imgIdx]) * box[0][0][3]);
              // ctx.lineTo((500 / imgW[imgIdx]) * box[0][0][2], (312 / imgH[imgIdx]) * box[0][0][3]);

              // ctx.moveTo((500 / imgW[imgIdx]) * box[0][0][0], (312 / imgH[imgIdx]) * box[0][0][1]);
              // ctx.lineTo((500 / imgW[imgIdx]) * box[0][0][0], (312 / imgH[imgIdx]) * box[0][0][3]);

              // ctx.closePath();
              // ctx.stroke();

              let polygon = bgJsonData.results[imgIdx].polygon;

              ctx.strokeStyle = "red";
              ctx.beginPath();

              for (let i = 0; i < polygon.length - 1; i++) {
                ctx.moveTo((500 / imgW[imgIdx]) * polygon[i][0], (312 / imgH[imgIdx]) * polygon[i][1]);
                ctx.lineTo((500 / imgW[imgIdx]) * polygon[i + 1][0], (312 / imgH[imgIdx]) * polygon[i][1]);
              }

              ctx.closePath();
              ctx.stroke();
            }
          }

          imgObj.src = result;

          resultImg.appendChild(typeTxt);
          resultImg.appendChild(popupImg);
          $panel.fadeIn();
        })

        let temp = new Array();
        let img = new Image();

        img.src = result;
        img.onload = function () {
          temp.push(this.width);
          temp.push(this.height);
          obj.filename.push(files[index].name);
          obj.image.push(result.split(',')[1]);
          obj.size_img.push(temp);
          obj.no_img = obj.no_img + 1;
          temp = [];

          imgW.push(this.width);
          imgH.push(this.height);

          idx++;
          readFile(index + 1);
        }
        thumbnailCon.appendChild(bg);
      }
    }
    readFile(0);
  })
})