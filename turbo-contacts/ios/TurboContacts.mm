#import "TurboContacts.h"

@implementation TurboContacts

RCT_EXPORT_MODULE()

- (instancetype)init {
    self = [super init];

    if (self) {
        self.contactsStore = [[CNContactStore alloc] init];
    }

    return self;
}

+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

- (NSArray<NSDictionary *> *)getAllContacts {
    NSMutableArray<NSDictionary *> *contacts = [NSMutableArray array];
    CNContactFetchRequest *request = [[CNContactFetchRequest alloc] initWithKeysToFetch:@[CNContactGivenNameKey, CNContactFamilyNameKey, CNContactPhoneNumbersKey]];
    NSError *error;

    [self.contactsStore enumerateContactsWithFetchRequest:request error:&error usingBlock:^(CNContact *contact, BOOL *stop) {
        if (error) {
            return;
        }
        [contacts addObject:@{
            @"firstName": contact.givenName,
            @"lastName": contact.familyName,
            @"phoneNumber": contact.phoneNumbers.count > 0
                ? contact.phoneNumbers[0].value.stringValue
                : [NSNull null]
        }];
    }];

    return contacts;
}

- (NSNumber *)hasContactsPermission {
    CNAuthorizationStatus authorizationStatus = [CNContactStore authorizationStatusForEntityType:CNEntityTypeContacts];

    return @(authorizationStatus == CNAuthorizationStatusAuthorized);
}

- (void)requestContactsPermission:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    [self.contactsStore requestAccessForEntityType:CNEntityTypeContacts completionHandler:^(BOOL granted, NSError *error) {
        if (error) {
            return reject(@"Error", @"An Error occurred while requesting permission", error);
        }
        resolve(@(granted));
    }];
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const facebook::react::ObjCTurboModule::InitParams &)params {
    return std::make_shared<facebook::react::NativeTurboContactsSpecJSI>(params);
}

@end
