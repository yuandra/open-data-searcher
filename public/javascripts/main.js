$(function(){

    $(".search-result").hide();
    $(".search-result").eq(0).show();


    $(".href-button-dataportal").click(function(){
        $(".search-result").hide();
        var tShow = this.getAttribute("param");
        $("#"+tShow).show();
    });

    function exportToCsv(filename, rows) {
        var processRow = function (row) {
            var finalVal = '';
            for (var j = 0; j < row.length; j++) {
                var innerValue = row[j] === null ? '' : row[j].toString();
                if (row[j] instanceof Date) {
                    innerValue = row[j].toLocaleString();
                };
                var result = innerValue.replace(/"/g, '""');
                if (result.search(/("|,|\n)/g) >= 0)
                    result = '"' + result + '"';
                if (j > 0)
                    finalVal += ',';
                finalVal += result;
            }
            return finalVal + '\n';
        };

        var csvFile = '';
        for (var i = 0; i < rows.length; i++) {
            csvFile += processRow(rows[i]);
        }

        var blob = new Blob([csvFile], { type: 'text/csv;charset=utf-8;' });
        if (navigator.msSaveBlob) { // IE 10+
            navigator.msSaveBlob(blob, filename);
        } else {
            var link = document.createElement("a");
            if (link.download !== undefined) { // feature detection
                // Browsers that support HTML5 download attribute
                var url = URL.createObjectURL(blob);
                link.setAttribute("href", url);
                link.setAttribute("download", filename);
                link.style.visibility = 'hidden';
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            }
        }
    }

    function ConvertToCSV(filename,objArray) {
        var array = typeof objArray != 'object' ? JSON.parse(objArray) : objArray;
        var str = '';

        for (var i = 0; i < array.length; i++) {
            var line = '';
            for (var index in array[i]) {
                if (line != '') line += ','

                line += "\""+array[i][index]+"\"";
            }

            str += line + '\r\n';
        }


        var blob = new Blob([str], { type: 'text/csv;charset=utf-8;' });
        if (navigator.msSaveBlob) { // IE 10+
            navigator.msSaveBlob(blob, filename);
        } else {
            var link = document.createElement("a");
            if (link.download !== undefined) { // feature detection
                // Browsers that support HTML5 download attribute
                var url = URL.createObjectURL(blob);
                link.setAttribute("href", url);
                link.setAttribute("download", filename);
                link.style.visibility = 'hidden';
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            }
        }

    }


    $(".href-button-exportcsv").click(function(){
        var tStringJSON = this.getAttribute("param");
        tStringJSON = tStringJSON.replace(/(\r\n|\n|\r)/gm,'');
        // var tEscapedJSONString = tStringJSON.replace(/\\n/g, "\\n")
        //     .replace(/\\'/g, "\\'")
        //     .replace(/\\"/g, '\\"')
        //     .replace(/\\&/g, "\\&")
        //     .replace(/\\r/g, "\\r")
        //     .replace(/\\t/g, "\\t")
        //     .replace(/\\b/g, "\\b")
        //     .replace(/\\f/g, "\\f");
        var tJSON = JSON.parse(tStringJSON);
        ConvertToCSV("searchresult.csv",tJSON);
    });

});
