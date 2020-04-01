export default {
    /**
     * @public
     * Returns string which shortened or long
     * @param value string to normalize
     * @param stringLength result string length
     * @return {string} normilized value
     */
    normalizeString: (value, stringLength = 15) => {
        let filteredValue = value
        if (value.length >= stringLength - 2) {
            filteredValue = value.slice(0, stringLength - 3) + '...'
        } else {
            let placeholder = '_'
            filteredValue += placeholder.repeat(stringLength - value.length)
        }
        return filteredValue
    }
}