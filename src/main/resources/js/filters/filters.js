export default {
    normalizeString: (value, stringLength = 15) => {
        let filteredValue = value
        if (value.length >= 13) {
            filteredValue = value.slice(0, 12) + '...'
        } else {
            let placeholder = '_'
            filteredValue += placeholder.repeat(15 - value.length)
        }
        return filteredValue
    }
}