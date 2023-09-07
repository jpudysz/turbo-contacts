import React, { useState } from 'react'
import { View, StyleSheet, Text, Pressable, ScrollView, Image, SafeAreaView } from 'react-native'
import { Contact, TurboContacts } from './turbo-contacts/spec'

const App = () => {
    const [hasPermission, setHasPermission] = useState(TurboContacts.hasContactsPermission())
    const [contacts, setContacts] = useState<Array<Contact>>([])

    return (
        <SafeAreaView style={styles.container}>
            <ScrollView contentContainerStyle={styles.container}>
                <Image
                    source={require('./hero.jpg')}
                    style={styles.hero}
                />
                <Text style={styles.heading}>
                    Turbo Contacts
                </Text>
                {!hasPermission && (
                    <Pressable
                        style={styles.button}
                        onPress={() => {
                            TurboContacts.requestContactsPermission()
                                .then(setHasPermission)
                        }}
                    >
                        <Text style={styles.buttonText}>
                            Request permission
                        </Text>
                    </Pressable>
                )}
                {hasPermission && (
                    <Pressable
                        style={styles.button}
                        onPress={() => setContacts(TurboContacts.getAllContacts())}
                    >
                        <Text style={styles.buttonText}>
                            Fetch contacts
                        </Text>
                    </Pressable>
                )}
                {contacts.length > 0 && (
                    <View style={styles.contactsContainer}>
                        {contacts.map((contact, index) => (
                            <View
                                key={index}
                                style={styles.contactTile}
                            >
                                <Text style={styles.name}>
                                    {index + 1}. {contact.firstName} {contact.lastName}
                                </Text>
                                <Text>
                                    {contact.phoneNumber ?? '-'}
                                </Text>
                            </View>
                        ))}
                    </View>
                )}
            </ScrollView>
        </SafeAreaView>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        backgroundColor: 'white',
    },
    contactTile: {
        flexDirection: 'row',
        alignSelf: 'flex-start',
        height: 30,
        justifyContent: 'space-between',
    },
    contactsContainer:{
        width: '100%',
        borderWidth: 1,
        borderRadius: 8,
        padding: 12,
        marginTop: 50,
    },
    hero: {
        height: 200,
        width: 300,
        resizeMode: 'contain',
    },
    heading: {
        fontWeight: 'bold',
        fontSize: 26,
    },
    button: {
        backgroundColor: '#D33257',
        padding: 10,
        borderRadius: 8,
        marginTop: 20,
    },
    buttonText: {
        color: 'white',
    },
    name: {
        flex: 1,
        fontWeight: 'bold',
    },
})

export default App
