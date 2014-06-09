function getTokens(stringToTokenize, delimiter) {
    var rows = [];
    var stringTokenizer = new jQuery.tokenizer([delimiter],
            function(text, isSeparator) {
                if (!isSeparator) {
                    rows.push(text);
                }
            }
    );
    stringTokenizer.parse(stringToTokenize);

    return rows;
}