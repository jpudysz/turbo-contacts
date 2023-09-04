#import "TurboContactsSpec.h"
#import <Contacts/Contacts.h>

@interface TurboContacts : NSObject<NativeTurboContactsSpec>
    @property CNContactStore *contactsStore;
@end
