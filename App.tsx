import React, { useState } from 'react'
import { View, StyleSheet, Text, Pressable, ScrollView } from 'react-native'
import { Contact, TurboContacts } from './turbo-contacts/spec'

const App = () => {
    const [hasPermission, setHasPermission] = useState(TurboContacts.hasContactsPermission())
    const [contacts, setContacts] = useState<Array<Contact>>([])

    return (
        <ScrollView contentContainerStyle={styles.container}>
            <Text>
                Has permission: {String(hasPermission)}
            </Text>
            {!hasPermission && (
                <Pressable
                    onPress={() => {
                        TurboContacts.requestContactsPermission()
                            .then(setHasPermission)
                    }}
                >
                    <Text>
                        Request permission
                    </Text>
                </Pressable>
            )}
            {hasPermission && (
                <Pressable onPress={() => setContacts(TurboContacts.getAllContacts())}>
                    <Text>
                        Fetch contacts
                    </Text>
                </Pressable>
            )}
            {contacts.map((contact, index) => (
                <View
                    key={index}
                    style={styles.contactTile}
                >
                    <Text>
                        {contact.firstName} {contact.lastName}
                    </Text>
                    <Text>
                        {contact.phoneNumber}
                    </Text>
                </View>
            ))}
        </ScrollView>
    )
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    contactTile: {
        flexDirection: 'row',
    },
})
export default App
