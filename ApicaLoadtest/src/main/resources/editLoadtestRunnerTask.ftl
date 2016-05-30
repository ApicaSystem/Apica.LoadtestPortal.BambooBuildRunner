[@ww.textfield labelKey="apicaloadtest.api.token" name="token" required='true' cssClass="long-field"/]
[@ww.textfield labelKey="apicaloadtest.preset" name="preset" required='true' cssClass="long-field"/]
[@ww.textfield labelKey="apicaloadtest.scenario" name="scenario" required='true' cssClass="long-field"/]

[@ui.bambooSection titleKey="apicaloadtest.thresholds.section"]
    <ul id="checkout-list">
        [#list selectedRepositoriesIndices?sort as index]
            <li data-checkout-id="${index}">[@repoSnippet index /]</li>
        [/#list]
    </ul>
[/@ui.bambooSection]

[#macro removeButton]
<div class="aui-toolbar inline">
    <ul class="toolbar-group">
        <li class="toolbar-item">
            <a class="toolbar-trigger">[@ww.text name="global.buttons.remove" /]</a>
        </li>
    </ul>
</div>
[/#macro]

[#macro repoSnippet index=2]        

    [@removeButton /]
    [@ww.textfield labelKey="apicaloadtest.thresholds.test" name="test" required="false" cssClass="long-field"/]
[/#macro]

<script type="text/x-template" title="checkout-list-item-template">
    [#assign repoSnippetInst][@repoSnippet 869576137068/][/#assign]
        <li data-checkout-id="{index}">${repoSnippetInst?replace("869576137068", "{index}")}</li>
</script>

<script type="text/javascript">
CheckoutTaskConfiguration.init({
            addCheckoutSelector: "#checkout-add",
            checkoutListSelector: "#checkout-list",
            checkoutDirectorySelector: ".checkout-directory:visible",
            templates: {
                checkoutListItem: "checkout-list-item-template"
            },
            i18n: {
                checkoutDirectoryInUse: "${checkoutDirectoryInUse?js_string}"
            }
});

CheckoutTaskConfiguration = (function () {
    var defaults = {
        addCheckoutSelector: null,
        removeCheckoutSelector: '.toolbar-trigger',
        checkoutListSelector: null,
        checkoutDirectorySelector: null,
        selectedRepositorySelector: 'select[name^="selectedRepository_"]',
        templates: {
            checkoutListItem: null
        },
        i18n: {
            checkoutDirectoryInUse: null
        }
    },
    options,
            $list,
            $form,
            addCheckoutListItem = function () {
                var newIndex, $lastCheckout;

                $lastCheckout = $list.children(':last');
                newIndex = ($lastCheckout.length ? (parseInt($lastCheckout.attr('data-checkout-id'), 10) + 1) : 0);

                $(AJS.template.load(options.templates.checkoutListItem).fill({index: newIndex}).toString())
                        .hide().appendTo($list)
                        .find(options.selectedRepositorySelector).end()
                        .slideDown();

                BAMBOO.DynamicFieldParameters.syncFieldShowHide($list);
            },
            removeCheckoutListItem = function () {
                $(this).closest('.aui-toolbar').closest('li').slideUp(function () {
                    $(this).remove();
                });
            },
            validateForm = function (e) {
                var checkoutDirectories = {},
                        hasError = false,
                        checkField = function () {
                            var $field = $(this),
                                    val = $field.val();

                            if (checkoutDirectories.hasOwnProperty(val)) {
                                $('<div/>', {'class': 'error', text: options.i18n.checkoutDirectoryInUse}).insertAfter($field.next('.description'));
                                hasError = true;
                            } else {
                                checkoutDirectories[val] = true;
                            }
                        },
                        $trigger = $(this),
                        $fieldsToCheck = $form.find(options.checkoutDirectorySelector);

                $form.find('.error').remove();

                if ($trigger.is(':text')) {
                    $fieldsToCheck = $fieldsToCheck.not($trigger);
                    $fieldsToCheck.each(checkField);
                    checkField.apply(this);
                } else {
                    $fieldsToCheck.each(checkField);
                    if (hasError) {
                        e.preventDefault();
                        e.stopPropagation();
                    }
                }
            };

    return {
        init: function (opts) {
            options = $.extend(true, defaults, opts);

            $(function () {
                $list = $(options.checkoutListSelector)
                        .delegate(options.removeCheckoutSelector, 'click', removeCheckoutListItem)
                        .delegate(options.checkoutDirectorySelector, 'blur', validateForm);

                $form = $list.closest('form').submit(validateForm);
                $(options.addCheckoutSelector).click(addCheckoutListItem);
            });
        }
    };
}());
</script>