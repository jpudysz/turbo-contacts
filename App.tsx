import React from 'react'
import { View, StyleSheet, Text } from 'react-native'
import { TurboContacts } from './turbo-contacts/spec'
const App = () => (
    <View style={styles.container}>
        <Text>
            {String(TurboContacts.hasContactsPermission())}
        </Text>
    </View>
)
const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
})
export default App
