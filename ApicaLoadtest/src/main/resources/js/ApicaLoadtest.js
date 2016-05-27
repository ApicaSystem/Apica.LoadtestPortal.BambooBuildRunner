function adjustIframeSource()
{
    alert('Div loaded');
    var iframe = document.getElementById("apica-ci-container");
    var sourceAttrib = iframe.getAttribute("src");
    var modifiedIframeSource = sourceAttrib.replace("&amp;", "&");
    iframe.setAttribute("src", modifiedIframeSource);
    alert('Source attrib');
}