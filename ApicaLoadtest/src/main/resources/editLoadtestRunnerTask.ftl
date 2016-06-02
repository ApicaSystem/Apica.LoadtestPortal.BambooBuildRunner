[@ui.bambooSection titleKey="apicaloadtest.basics.section"]
    [@ww.textfield labelKey="apicaloadtest.api.token" name="token" required='true' cssClass="long-field"/]
    [@ww.textfield labelKey="apicaloadtest.preset" name="preset" required='true' cssClass="long-field"/]
    [@ww.textfield labelKey="apicaloadtest.scenario" name="scenario" required='true' cssClass="long-field"/]
[/@ui.bambooSection]

[@ui.bambooSection titleKey="apicaloadtest.thresholds.section"]
    <ul id="checkout-list" style="border-bottom: 1px solid #ddd;list-style:none;margin:0 0 10px; padding:0">
        [#list selectedThresholdIndices?sort as index]
            <li data-threshold-id="${index}" style="margin:0;padding-bottom:10px;position:relative" >[@thresholdSnippet index /]</li>
        [/#list]
    </ul>

    <a id="threshold-add">[@ww.text name="apicaloadtest.thresholds.add"/]</a>
[/@ui.bambooSection]

[#macro removeButton]
<div class="aui-toolbar inline" style="position:absolute; right:0; top; 14px; z-index:1">
    <ul class="toolbar-group">
        <li class="toolbar-item">
            <a class="toolbar-trigger">[@ww.text name="global.buttons.remove" /]</a>
        </li>
    </ul>
</div>
[/#macro]

[#macro thresholdSnippet index=0]
    [@removeButton /]
    [@ww.select labelKey='apicaloadtest.thresholds.metric' name='threshold_metric_${index}' list='thresholdMetrics' toggle=true cssClass="metric"/]
    [@ww.select labelKey='apicaloadtest.thresholds.direction' name='threshold_direction_${index}' list='thresholdDirections' toggle=true/]
    [@ww.textfield labelKey="apicaloadtest.thresholds.value" name="threshold_value_${index}" required="false" cssClass="threshold-value"/]
[/#macro]

<script type="text/x-template" title="threshold-list-item-template">
    [#assign thresholdSnippetInst][@thresholdSnippet 869576137068/][/#assign]
    <li data-threshold-id="{index}">${thresholdSnippetInst?replace("869576137068", "{index}")}</li>
</script>

<script type="text/javascript">
Thresholdfiguration = (function () {
    var defaults = {
        addThresholdSelector: null,
        removeThresholdSelector: '.toolbar-trigger',
        thresholdListSelector: null,   
        thresholdValueSelector: null,
        selectedThresholdSelector: 'textfield[name^="threshold_value_"]',
        templates: {
                    thresholdListItem: null
                }
    },
    options,
            $list,
            $form,
            addthresholdListItem = function () {
                var newIndex, $lastThreshold;

                $lastThreshold = $list.children(':last');
                newIndex = ($lastThreshold.length ? (parseInt($lastThreshold.attr('data-threshold-id'), 10) + 1) : 0);      
                jQuery(AJS.template.load(options.templates.thresholdListItem).fill({index: newIndex}).toString())
                        .hide().appendTo($list)
                        .find(options.selectedThresholdSelector).end()
                        .slideDown();

                BAMBOO.DynamicFieldParameters.syncFieldShowHide($list);
            },
            removethresholdListItem = function () {
                jQuery(this).closest('.aui-toolbar').closest('li').slideUp(function () {
                    jQuery(this).remove();
                });
            },
            validateForm = function (e) {
                    hasError = false,
                    checkField = function () {
                        var $field = jQuery(this),  val = $field.val();
                        var idValueToSearch = $field.attr('id').replace("threshold_value_", "threshold_metric_");
                        var selectBox = jQuery('#' + idValueToSearch);                        
                        var selectedMetric = selectBox.children("option").filter(":selected").text();
                        if (!jQuery.isNumeric(val) || val < 0)
                        {
                            jQuery('<div/>', { 'class': 'error', text: 'You must enter a positive number' }).insertAfter($field.next('.description'));
                            hasError = true;
                        }                        
                        
                        if (selectedMetric.indexOf("%") >= 0)
                        {
                            if (val < 0 || val > 100)
                            {
                                jQuery('<div/>', { 'class': 'error', text: 'Percentage values must range from 0 to 100' }).insertAfter($field.next('.description'));
                                hasError = true;
                            }
                        }
                    },
                    $trigger = jQuery(this),
                    $fieldsToCheck = $form.find(options.thresholdValueSelector);

                $form.find('.error').remove();                
               
                $fieldsToCheck.each(checkField);
                if (hasError) {
                    e.preventDefault();
                    e.stopPropagation();
                }                
            };

    return {
        init: function (opts) {
            options = jQuery.extend(true, defaults, opts);

            jQuery(function () {
                $list = jQuery(options.thresholdListSelector)
                        .delegate(options.removeThresholdSelector, 'click', removethresholdListItem);
                $form = $list.closest('form').submit(validateForm);
                jQuery(options.addThresholdSelector).click(addthresholdListItem);
            });
        }
    };
}());

Thresholdfiguration.init({
            addThresholdSelector: "#threshold-add",
            thresholdListSelector: "#checkout-list",
            thresholdValueSelector: ".threshold-value:visible",
            templates: {
                    thresholdListItem: "threshold-list-item-template"
            }            
});
</script>