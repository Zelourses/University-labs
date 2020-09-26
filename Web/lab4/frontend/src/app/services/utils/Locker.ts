export function Locker( block: () => any): any {
    try {
        block();
    } finally {
    }
}
// важное говно, применяется правда всего один раз:
export function locker2(block: () => Promise<boolean>): Promise<boolean> {
    try {
        return block();
    } catch (e) {
        return new Promise<boolean>(() => {return true;} );
    }

}
