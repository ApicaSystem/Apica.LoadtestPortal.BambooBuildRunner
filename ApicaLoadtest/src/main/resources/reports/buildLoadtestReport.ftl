<html>
<head>
    <meta name="tab" content="View Apica Loadtest Summary"/>
    <meta name="decorator" content="result"/>
    <link rel="stylesheet" type="text/css" href="${baseUrl}/download/resources/${groupId}.${artifactId}/ApicaLoadtest.css" />
</head>

<body>
    <div class="section">

        <div id="self-service-summary">
                <img src="${baseUrl}/download/resources/${groupId}.${artifactId}/apica-loadtest-logo.png"/>
                <h2>Apica Load Test Summary</h2>
                <table>
                    <tr>
                        <th>Test has finished</th>
                        <td>${testHasFinished}</td>
                    </tr>
                    <tr>
                        <th>Test has result</th>
                        <td>${testHasResult}</td>
                    </tr>                
                </table>
        </div>
    </div>
</body>
</html>