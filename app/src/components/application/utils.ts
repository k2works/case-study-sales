export const showErrorMessage = (error: string, callback: (error: string) => void) => {
    console.error(error);
    callback(error);
};
