import type { TurboModule } from 'react-native'
import { TurboModuleRegistry } from 'react-native'

export type Contact = {
    firstName: string,
    lastName: string,
    phoneNumber: string | null
}
export interface Spec extends TurboModule {
    hasContactsPermission(): boolean,
    requestContactsPermission(): Promise<boolean>,
    getAllContacts(): Array<Contact>
}

export default TurboModuleRegistry.getEnforcing<Spec>('TurboContacts')
