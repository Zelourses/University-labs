import {ToasterService} from '../toaster.service';

export async function BackendApiToaster(toaster: ToasterService, response: Promise<Response>, exclude: number[] = []):Promise<Response> {
    try {
        let responded = await response;
        if (responded.ok && !exclude.includes(responded.status)) {
            switch (responded.status) {
                case 400:
                    toaster.error('Плохой запрос', 'Похоже, вы неправильно что-то задали. Пожалуйста, попробуйте позже');
                    break;
            }// TODO рассмотреть другие случаи(какие?)
        }
        return response;
    } catch (e) {
        toaster.error('Нет соединения', 'Похоже, соединение с сервером исчезло. Проверьте настройки вашего интернета');
        throw e;
    }
}
