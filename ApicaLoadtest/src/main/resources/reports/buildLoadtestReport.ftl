<html>
<head>
    <meta name="tab" content="View Apica Loadtest Summary"/>
    <meta name="decorator" content="result"/>
</head>

<body>
    <div class="section">

        <div id="self-service-summary">
                <img src="${baseUrl}/download/resources/${groupId}.${artifactId}/apica-loadtest-logo.png"/>
                <h2>Apica Load Test Summary</h2>
                <table style="border-collapse: separate;">
                    <tr>
                        <th style="text-align: left;padding: 0.25em;padding-right: 1em;vertical-align: top;background-color: #115822;color: #ffffff;">Preset name</th>
                        <td style="width: 40em;text-align: left;padding: 0.25em;padding-left: .5em;vertical-align: top;background-color: #ebebeb;">${presetName}</td>
                    </tr>
                    <tr>
                        <th style="text-align: left;padding: 0.25em;padding-right: 1em;vertical-align: top;background-color: #115822;color: #ffffff;">Scenario</th>
                        <td style="width: 40em;text-align: left;padding: 0.25em;padding-left: .5em;vertical-align: top;background-color: #ebebeb;">${scenario}</td>
                    </tr>
                    <tr>
                        <th style="text-align: left;padding: 0.25em;padding-right: 1em;vertical-align: top;background-color: #115822;color: #ffffff;">Start date UTC</th>
                        <td style="width: 40em;text-align: left;padding: 0.25em;padding-left: .5em;vertical-align: top;background-color: #ebebeb;">${startDateUtc}</td>
                    </tr>                
                    <tr>
                        <th style="text-align: left;padding: 0.25em;padding-right: 1em;vertical-align: top;background-color: #115822;color: #ffffff;">Total passed loops</th>
                        <td style="width: 40em;text-align: left;padding: 0.25em;padding-left: .5em;vertical-align: top;background-color: #ebebeb;">${totalPassedLoops}</td>
                    </tr>
                    <tr>
                        <th style="text-align: left;padding: 0.25em;padding-right: 1em;vertical-align: top;background-color: #115822;color: #ffffff;">Total failed loops</th>
                        <td style="width: 40em;text-align: left;padding: 0.25em;padding-left: .5em;vertical-align: top;background-color: #ebebeb;">${totalFailedLoops}</td>
                    </tr>
                    <tr>
                        <th style="text-align: left;padding: 0.25em;padding-right: 1em;vertical-align: top;background-color: #115822;color: #ffffff;">Average network throughput</th>
                        <td style="width: 40em;text-align: left;padding: 0.25em;padding-left: .5em;vertical-align: top;background-color: #ebebeb;">${averageNetworkThroughput} ${networkThroughputUnit}</td>
                    </tr>
                    <tr>
                        <th style="text-align: left;padding: 0.25em;padding-right: 1em;vertical-align: top;background-color: #115822;color: #ffffff;">Average session time per loop (s)</th>
                        <td style="width: 40em;text-align: left;padding: 0.25em;padding-left: .5em;vertical-align: top;background-color: #ebebeb;">${averageSessionTimePerLoop}</td>
                    </tr>
                    <tr>
                        <th style="text-align: left;padding: 0.25em;padding-right: 1em;vertical-align: top;background-color: #115822;color: #ffffff;">Average response time per loop (s)</th>
                        <td style="width: 40em;text-align: left;padding: 0.25em;padding-left: .5em;vertical-align: top;background-color: #ebebeb;">${averageResponseTimePerLoop}</td>
                    </tr>
                    <tr>
                        <th style="text-align: left;padding: 0.25em;padding-right: 1em;vertical-align: top;background-color: #115822;color: #ffffff;">Web transaction rate (Hits/s)</th>
                        <td style="width: 40em;text-align: left;padding: 0.25em;padding-left: .5em;vertical-align: top;background-color: #ebebeb;">${webTransactionRate}</td>
                    </tr>
                    <tr>
                        <th style="text-align: left;padding: 0.25em;padding-right: 1em;vertical-align: top;background-color: #115822;color: #ffffff;">Average response time per page (s)</th>
                        <td style="width: 40em;text-align: left;padding: 0.25em;padding-left: .5em;vertical-align: top;background-color: #ebebeb;">${averageResponseTimePerPage}</td>
                    </tr>
                    <tr>
                        <th style="text-align: left;padding: 0.25em;padding-right: 1em;vertical-align: top;background-color: #115822;color: #ffffff;">Total http(s) calls</th>
                        <td style="width: 40em;text-align: left;padding: 0.25em;padding-left: .5em;vertical-align: top;background-color: #ebebeb;">${totalHttpCalls}</td>
                    </tr>
                    <tr>
                        <th style="text-align: left;padding: 0.25em;padding-right: 1em;vertical-align: top;background-color: #115822;color: #ffffff;">Avg network connect time (ms)</th>
                        <td style="width: 40em;text-align: left;padding: 0.25em;padding-left: .5em;vertical-align: top;background-color: #ebebeb;">${averageNetworkConnectTime}</td>
                    </tr>
                    <tr>
                        <th style="text-align: left;padding: 0.25em;padding-right: 1em;vertical-align: top;background-color: #115822;color: #ffffff;">Total transmitted bytes</th>
                        <td style="width: 40em;text-align: left;padding: 0.25em;padding-left: .5em;vertical-align: top;background-color: #ebebeb;">${totalTransmittedBytes}</td>
                    </tr>
                    <tr>
                        <th style="text-align: left;padding: 0.25em;padding-right: 1em;vertical-align: top;background-color: #115822;color: #ffffff;">Link to details</th>
                        <td style="width: 40em;text-align: left;padding: 0.25em;padding-left: .5em;vertical-align: top;background-color: #ebebeb;">${linkToDetails}</td>
                    </tr>
                </table>
        </div>
    </div>
</body>
</html>