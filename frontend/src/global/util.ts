export function toYYYYMMDD(date: Date) {
    const date1 = new Date(date);
    const year: string = date1.getFullYear().toString();
    let month: string = (date1.getMonth() + 1).toString();
    let day: string = date1.getDate().toString();

    if (month.length < 2) {
        month = "0" + month;
    }
    if (day.length < 2) {
        day = "0" + day;
    }

    return [year, month, day].join("-");
}

export function range(num: number) {
    return Array(num)
        .fill(0)
        .map((_, i) => i);
}
